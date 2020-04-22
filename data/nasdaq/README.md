# Nasdaq ITCH 5.0 Parser
Parses [Nasdaq ITCH 5.0](http://www.nasdaqtrader.com/content/technicalsupport/specifications/dataproducts/NQTVITCHspecification.pdf) message tar file and transforms into CSV chunked by hour

## Usage
1. Download a ITCH file from ftp://emi.nasdaq.com/ITCH/
2. `cd data/nasdaq`
3. `python parser.py <path_to_ITCH_file>`
  > python parser.py 01302020.NASDAQ_ITCH50.gz

4. This will run for a while and spit out files in the format of `output/<hour>.txt`


## Output

Example: `output/00.txt`

```
time,symbol,price,volume
00:00:00,UGAZ,44.42,500
00:00:00,EQT,6.0,1
00:00:00,QRVO,120.66,15
00:00:01,CCL,43.76,38
00:00:01,SQ,74.64,80
00:00:02,UPS,114.57,100
...
```
