import os
import pandas as pd
import time
import numpy as np

os.chdir('c:/code')  

f = open('01302019.NASDAQ_ITCH50', 'rb')

stock_frame = pd.DataFrame(index=pd.MultiIndex.from_tuples([('', '')],
                                                            names = ['Stock Name','Metric']),
                           columns=pd.Index(['8:00am', '9:00am', '10:00am',
                                             '11:00am', '12:00pm', '1:00pm',
                                             '2:00pm', '3:00pm', '4:00pm']))

stock_frame.sort_index()


def stock_register(record):  # Function adds stock names to a Stock Registry DataFrame

    stock_name = record[11:19].strip().decode('ascii')
    global stock_frame

    if stock_name not in stock_frame:  # Checks if name already exists in frame.
        df_stock = pd.DataFrame(np.zeros((3,9)),
                                index=pd.MultiIndex.from_tuples([(stock_name, 'Price*Volume'),
                                                                 (stock_name, 'Volume'),
                                                                 (stock_name, 'VWAP')]),
                                columns=pd.Index(['8:00am', '9:00am', '10:00am',
                                                  '11:00am', '12:00pm', '1:00pm',
                                                  '2:00pm', '3:00pm', '4:00pm']))
        ## print('Registering ', stock_name)
        stock_frame = stock_frame.append(df_stock)


def get_time(record):

    global output
    time_stamp = int.from_bytes(record[5:11], byteorder='big', signed=False)
    t = pd.Timestamp(time_stamp).round('60min').hour
    if t < 8:
        output = '8:00am'
    elif t < 12:
        output = str(t) + ':00am'
    elif t >= 12:
        t = t-12
        output = str(t) + ':00pm'

    #  print(output)

    return output


def no_mpid_add(record):  # Function processes Add Order-No MPID Attribution

    if record[19:20].decode('ascii') == 'S' or record[19:20].decode('ascii') == 'B':  # Runs if Sell Order ignores
        #  input('Add order. Press enter to continue..................')
        #  time_stamp = int.from_bytes(record[5:11], byteorder='big', signed=False)
        #  t = pd.Timestamp(time_stamp).round('60min').hour
        hour = get_time(record)
        price = int.from_bytes(record[32:36], byteorder='big', signed=False)/10000 # Four Decimal Places
        volume = int.from_bytes(record[20:24], byteorder='big', signed=False)
        stock_name = record[24:32].strip().decode('ascii')
        pv = price*volume
        print('Adding ', stock_name,' at ', hour)
        stock_frame.loc[(stock_name, 'Price*Volume'),
                        hour] = stock_frame.loc[(stock_name,
                                                     'Price*Volume'), hour] + pv
        stock_frame.loc[(stock_name, 'Volume'),
                        hour] = stock_frame.loc[(stock_name,
                                                     'Volume'), hour] + volume
        stock_frame.loc[(stock_name, 'VWAP'),
                        hour] = stock_frame.loc[(stock_name, 'Price*Volume'), hour]/stock_frame.loc[(stock_name, 'Volume'), hour]
        

start_time = time.time()

byte_counter = 0
skip_messages = [12,25,20,26,19,40,35,23,31,44,50]
while True:
    message_size = int.from_bytes(f.read(2), byteorder='big', signed=False)
    byte_counter = byte_counter + 2 + message_size
    if not message_size:
        print('no message')
        break  # Stops Program if no message was collected

    if message_size == 36:
        message = f.read(36)
        no_mpid_add(message)

    if message_size in skip_messages: # Skip system and MWCB messages (Assumed not important for VWAP)
        #  print('Skipped Message of size ', message_size)

    elif message_size == 39:
        message = f.read(39)
        stock_register(message)

print('Time Elapsed: {:.2f}s'.format(time.time() - start_time))
stock_frame.to_csv (r'C:\Users\Castellano\Desktop\export_dataframe.csv', index = True, header=True)
