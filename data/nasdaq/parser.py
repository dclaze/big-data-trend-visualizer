import datetime
import struct
import time

import gzip
import os
import pandas as pd
import sys

TRADE_MESSAGE_TYPE="P"

class ITCHTradeMessageIterator:
    def __init__(self, file):
        self.file = file
        self.message_header = None

    def __iter__(self):
        return self

    def get_binary(self, size):
        read = self.file.read(size)

        return read

    def next(self):
        self.message_header = self.get_binary(1)

        while self.message_header:
            if self.is_trade_message_header(self.message_header):
                message_length = self.get_next_message_length(self.message_header)
                return self.parse_trade_message(self.get_binary(message_length))
            else:
                message_length = self.get_next_message_length(self.message_header);
                if message_length:
                    message = self.get_binary(message_length);
                    # print(message_length, message)
                self.message_header = self.get_binary(1)

        raise StopIteration

    def is_trade_message_header(self, message_header):
        return message_header == TRADE_MESSAGE_TYPE

    def parse_trade_message(self, message):
        temp_message = struct.unpack('>4s6sQcI8cIQ', message)
        trade_message = struct.pack('>s4s2s6sQsI8sIQ', TRADE_MESSAGE_TYPE, temp_message[0], '\x00\x00', temp_message[1], temp_message[2], temp_message[3], temp_message[4],
                              ''.join(list(temp_message[5:13])), temp_message[13], temp_message[14])
        trade_values = struct.unpack('>sHHQQsI8sIQ', trade_message)
        trade_values_list = list(trade_values)

        time = self.convert_time(trade_values_list[3])
        hour = self.parse_chunk_key(time)
        symbol = trade_values_list[7].strip()
        price_as_float = float(trade_values_list[8])
        price = price_as_float / 10000
        volume = trade_values_list[6]

        return hour, [time, symbol, price, volume]

    def parse_chunk_key(self, time):
        # default chunk key is hourly
        return time.split(':')[0]

    def convert_time(self, stamp):
        time = datetime.datetime.fromtimestamp(stamp / 1e9)
        time = time.strftime('%H:%M:%S')
        return time

    def get_next_message_length(self, pointer):
        if pointer == "S":
            return 11
        elif pointer == "R":
            return 38
        elif pointer == "H":
            return 24
        elif pointer == "Y":
            return 19
        elif pointer == "L":
            return 25
        elif pointer == "V":
            return 34
        elif pointer == "W":
            return 11
        elif pointer == "K":
            return 27
        elif pointer == "A":
            return 35
        elif pointer == "F":
            return 39
        elif pointer == "E":
            return 30
        elif pointer == "C":
            return 35
        elif pointer == "X":
            return 22
        elif pointer == "D":
            return 18
        elif pointer == "U":
            return 34
        elif pointer == "P":
            return 43
        elif pointer == "Q":
            return 39
        elif pointer == "B":
            return 18
        elif pointer == "I":
            return 49
        elif pointer == "N":
            return 19
        else:
            return None

class ITCHFileChunker:
    def __init__(self):
        self.output_dir = 'output'
        self.header = ['time', 'symbol', 'price', 'volume']

    def write(self, key, data):
        path = os.path.join(self.output_dir, str(key) + '.txt')
        data_frame = pd.DataFrame(data=data, columns=self.header)
        data_frame.to_csv(path, index=False)

#########################################################
# NASDAQ_ITCH Trade Parser
# Parses NASDAQ_ITCH files into chunks defaults to hourly
#
# Usage: `python parse.py <filename>`
# Example: python parse.py <filename>
#########################################################
if __name__ == '__main__':
    start_time = datetime.datetime.now()

    print "Started parsing at", start_time.strftime("%H:%M:%S")

    file = gzip.open(sys.argv[1], 'rb')

    current_key = None
    current_key_data = []
    file_chunker = ITCHFileChunker()

    for key, message in ITCHTradeMessageIterator(file):
        if key != current_key:
            if current_key_data:
                finished_key_time=datetime.datetime.now()
                minutes = round((finished_key_time - start_time).total_seconds() / 60, 1)
                print "Writing chunk for key", current_key, "at", finished_key_time.strftime("%H:%M:%S"), "in", minutes,"minutes"
                file_chunker.write(current_key, current_key_data)
            current_key = key
            current_key_data = []
            current_key_data.append(message)
        else:
            current_key = key
            current_key_data.append(message)

    if current_key and current_key_data:
        finished_key_time=datetime.datetime.now()
        minutes = round((finished_key_time - start_time).total_seconds() / 60, 1)
        print "Writing chunk for key", current_key, "at", finished_key_time.strftime("%H:%M:%S"), "in", minutes,"minutes"
        file_chunker.write(current_key, current_key_data)

    end_time = datetime.datetime.now()
    print "Finished parsing at", end_time
    print "Total minutes", round((end_time - start_time).total_seconds() / 60, 1)
