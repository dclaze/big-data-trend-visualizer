import struct
import pickle
import numpy
from datetime import timedelta
import multiprocessing as mp


tick_count = 0
executed_order_count = 0
trade_message_count = 0
cross_trade_message_count = 0
object_list = {}
stock_map = {}
executing_order_map = {}

def add_order_message(message):
    global object_list
    if message[19:20].decode('ascii') == 'B':
        order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
        order_time_hours = timedelta(seconds=order_time)
        print('.......................Buy Order at', order_time_hours)
        order_ref_no =struct.unpack("!Q", message[11:19])[0]
        #print('Reference No.',order_ref_no)
        stock_name = message[24:32].strip().decode('ascii')
        #print('Name',stock_name)
        stock_price = int.from_bytes(message[32:36],byteorder='big',signed=False)/10000.00
        #print('Price',stock_price)
        object_list[order_ref_no] = (stock_name, stock_price)
        return
    return


def replace_order_message(message):
    global object_list

    order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
    order_time_hours = timedelta(seconds=order_time)
    #print('.......................Replace Order at', order_time_hours)
    old_order_ref_num = struct.unpack("!Q", message[11:19])[0]
    #print('Old Order Reference No.', old_order_ref_num)
    new_order_ref_num = struct.unpack('!Q', message[19:27])[0]
    #print('New Order Reference No.', new_order_ref_num)

    try:
        (stock_name, stock_price) = object_list.pop(old_order_ref_num)
        object_list[new_order_ref_num] = (stock_name, stock_price)
    except KeyError as e:
        return
    return

def delete_order_message(message):
    global object_list
    order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
    order_time_hours = timedelta(seconds=order_time)
    #print('.......................Delete Order at', order_time_hours)
    order_ref_no = int.from_bytes(message[11:19],byteorder='big',signed=False)
    #print('Order ref No. is ',order_ref_no)
    

    try:
        object_list.pop(order_ref_no)
    except KeyError as e:
        #input("Did not find key in object list. Press Enter to continue...")
        return
    return


def executed_price_order_message(message):
    global object_list
    global stock_map
    global executed_order_count
    global executing_order_map

    mType = 'C'
    order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
    order_time_hours = timedelta(seconds=order_time)
    #print('.......................Executed Price Order at', order_time_hours)
    ##is order printable?
    if message[31:32]=='Y': #Ignore messages marked as non-printable to avoid double counting
        order_ref_no = struct.unpack1("!Q", message[11:19])[0]
        #print('Order Reference Number is',order_ref_no)
        stock_price = int.from_bytes(message[32:36],byteorder='big',signed=False)/10000.00
        #print('Stock Price is ', stock_price)
        share_volume = struct.unpack("!I", message[19:23])[0]
        #print('Share Volume is ',share_volume)
        match_number = int.from_bytes(message[23:31],byteorder='big',signed=False)
        #match_number = struct.unpack("!Q", message[23:31])[0]
        try:
            (stock_name, stock_price_old) = object_list[order_ref_no]
            if stock_name not in stock_map:
                stock_map[stock_name] = [(mType, order_ref_no, stock_price, share_volume)]
            else:
                stock_list = stock_map[stock_name]
                stock_list.append((mType, order_ref_no, stock_price, share_volume))
                stock_map[stock_name] = stock_list
            #add order to executed order map
            executing_order_map[match_number] = (mType, order_ref_no, stock_name)
            executed_order_count+=1
        except KeyError as e:
            #print ("Order number: " . str(order_ref_no))
            #raw_input("Did not find key in object list. Press Enter to continue...")
            return
        return
    return

def executed_order_message(message):
    global executed_order_count
    global stock_map
    global object_list
    global executig_order_map

    mType = 'E'
    order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
    order_time_hours = timedelta(seconds=order_time)
    #print('.......................Executed Order at', order_time_hours)
    order_ref_no = struct.unpack("!Q", message[11:19])[0]
    #print(order_ref_no)
    stock_price = 0
    share_volume = struct.unpack("!I", message[19:23])[0]
    #print(share_volume)
    match_number = int.from_bytes(message[23:31],byteorder='big',signed=False)
    #match_number = struct.unpack("!Q", message[23:31])[0]
    try:
        (stock_name, stock_price) = object_list[order_ref_no]
        if stock_name not in stock_map:
            stock_map[stock_name] = [(mType, order_ref_no, stock_price, share_volume)]
        else:
            stock_list = stock_map[stock_name]
            stock_list.append((mType, oder_ref_no,stock_price, share_volume))
            stock_map[stock_name] = stock_list
            #add order to executed order map
            executing_order_map[match_number] = (mType, order_ref_no, stock_name)
            executed_order_count +=1
    except KeyError as e:
            #print("Order Number: ",order_ref_no)
            #input("Did not find key in object list. Press Enter to continue...")
        return
    return

def trade_message(message):
        global trade_message_count
        global stock_map
        global object_list
        global executing_order_map

        mType = 'P'
        order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
        order_time_hours = timedelta(seconds=order_time)
        #print('.......................Trade Order at', order_time_hours)
        order_ref_no = struct.unpack("!Q", message[11:19])[0]
        trade_message_count +=1
        stock_price = (struct.unpack("!I", message[32:36])[0])/10000.00
        #print(stock_price)
        share_volume = struct.unpack("!I", message[20:24])[0]
        #print(share_volume)
        match_number = struct.unpack("!Q", message[23:31])[0]
        stock_name = message[24:32].decode('ascii')
        #print(stock_name)
        if stock_name not in stock_map:
            stock_map[stock_name] = [(mType, order_ref_no, stock_price, share_volume)]
        else:
            stock_list = stock_map[stock_name]
            stock_list.append((mType, order_ref_no,stock_price, share_volume))
            stock_map[stock_name] = stock_list
        #Add order to executed order map
        executing_order_map[match_number] = (mType, order_ref_no, stock_name)
        return

def cross_trade_message(message):
    global cross_trade_message_count
    global stock_map
    global object_list
    global executing_order_map

    mType = 'Q'
    order_time = int.from_bytes(message[5:11],byteorder='big',signed=False)*1e-9
    order_time_hours = timedelta(seconds=order_time)
    #print('.......................Cross Trade Order at', order_time_hours)
    stock_price = (struct.unpack("!I", message[27:31])[0])/10000.00
    #print(stock_price)
    share_volume = struct.unpack("!Q", message[11:19])[0]
    #print(share_volume)
    match_number = struct.unpack("!Q", message[31:39])[0]
    stock_name = message[19:27].decode('ascii')
    #print(stock_name)
    
    if share_volume ==0:
        return
    elif stock_name not in stock_map:
        stock_map[stock_name] = [(mType, match_number, stock_price, share_volume)]
    else:
        stock_list = stock_map[stock_name]
        stock_list.append((mType, match_number,stock_price, share_volume))
        stock_map[stock_name] = stock_list
    #add order to executed order map
    executing_order_map[match_number] = (mType, match_number, stock_name)
    cross_trade_message_count+=1
    return

def broken_trade_message(message):
    global stock_map
    global object_list
    global executing_order_map

    #mType = 'B'
    match_number = int.from_bytes(message[11:19],byteorder='big',signed=False)
    #print(match_number)
    try:
        (mType, order_ref_no, stock_name) = executing_order_map.pop(match_number)
        if stock_name in stock_map:
            stock_list = stock_map[stock_name]
            for index, item in enumerate(stock_list):
                if item[1] == order_ref_no and mType == item[0]:
                    del stock_list[index]
                    break
            stock_map[stock_name] = stock_list
    except KeyError as e:
        return        

f = open('01302019.NASDAQ_ITCH50','rb');


while True:
    
    message_size = int.from_bytes(f.read(2),byteorder='big',signed=False)
    if not message_size:
        break
    #print(message_size)
    mtype = f.read(1).decode('ascii')
    #print(mtype)

    
    record = f.read(message_size-1)
    if not record:
        break
    if mtype =='A' or mtype =='F': #Parse message if Add Order
        #print('Message size is ',message_size)
        #print('mtype is',mtype)
        add_order_message(record)
    elif mtype =='U': #Else Parse message if Replace Order
        replace_order_message(record)
    elif mtype =='D': #Else Parse message if Delete Order
        delete_order_message(record)
    elif mtype =='C': #Else Parse message if Executed Order
        executed_price_order_message(record)
    elif mtype =='E':
        executed_order_message(record)
    elif mtype =='P':
        trade_message(record)
    elif mtype =='Q':
        cross_trade_message(record)
    elif mtype =='B':
        broken_trade_message(record)
    

print('Writing data to files....')
output_file = open('output_file.txt','a')
output_file.write('Total number of ticks: ' + str(tick_count))
output_file.write("Total number of executed orders: " + str(executed_order_count))
output_file.write("Total number of trade messages: " + str(trade_message_count))
output_file.write("Total number of cross trade message: " + str(cross_trade_message_count))

pickle.dump(stock_map, open('stock_dictionary.d','wb'))


for key, value in iter(stock_map.items()):
    workbook = xlsxwriter.Workbook(key+'t.xlsx')
    worksheet = workbook.add_worksheet()
    cumulative_volume = 0
    cumulative_volume_price = 0
    worksheet.write('A1', "Price")
    worksheet.write('B1', "Volume")
    worksheet.write('C1', "Cumulative Volume")
    worksheet.write('D1', "Cumulative Volume * Price")
    worksheet.write('E1', "VWAP")
    for index, item in enumerate(value):
        worksheet.write("A"+str(index+2), item[2])
        worksheet.write("B"+str(index+2), item[3])
        cumulative_volume+=item[3]
        cumulative_volume_price+= item[2] * item[3]
        worksheet.write("C"+str(index+2), cumulative_volume)
        worksheet.write("D"+str(index+2), cumulative_volume_price)
        worksheet.write("E"+str(index+2), cumulative_volume_price / (cumulative_volume * 1.00))        
f.close()

