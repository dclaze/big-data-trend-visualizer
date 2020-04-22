# NASDAQ_ITCH 5.0 Parser and VWAP

Parses [Nasdaq ITCH 5.0](http://www.nasdaqtrader.com/content/technicalsupport/specifications/dataproducts/NQTVITCHspecification.pdf) 
message tar file and calculates the VWAP for every stock at one hour intervals. 
Outputs into a CSV 

## Usage
1. Download a ITCH file from ftp://emi.nasdaq.com/ITCH/
2. 
3. `python Castellano_VWAP.py 
  > python Castellano_VWAP.py 

4. This will run for a while and spit out files in the format of:

Stock Name,Metric (Price*Volume, Volume, VWAP) ,8:00am,9:00am,10:00am,11:00am,12:00pm,1:00pm,2:00pm,3:00pm,4:00pm


## Output

Example: `output.txt`

```
Stock Name,Metric,8:00am,9:00am,10:00am,11:00am,12:00pm,1:00pm,2:00pm,3:00pm,4:00pm
,,,,,,,,,,
A,Price*Volume,17240.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
A,Volume,250.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
A,VWAP,68.96,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AA,Price*Volume,2594.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AA,Volume,100.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AA,VWAP,25.94,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AAAU,Price*Volume,4065.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AAAU,Volume,300.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
AAAU,VWAP,13.55,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
...
...
ZVZZT,Price*Volume,4200.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZVZZT,Volume,300.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZVZZT,VWAP,14.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZWZZT,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZWZZT,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZWZZT,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXIET,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXIET,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXIET,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXYZ.A,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXYZ.A,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXYZ.A,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXZZT,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXZZT,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZXZZT,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYME,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYME,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYME,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYNE,Price*Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYNE,Volume,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
ZYNE,VWAP,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
...
```
