# Nasdaq ITCH 5.0 Parser
Parses [Nasdaq ITCH 5.0](http://www.nasdaqtrader.com/content/technicalsupport/specifications/dataproducts/NQTVITCHspecification.pdf) message tar file and transforms into CSV chunked by hour

## Usage
1. Download a ITCH file from ftp://emi.nasdaq.com/ITCH/
2. `cd data/nasdaq`
3. `python parser.py <path_to_ITCH_file>`
  > python parser.py 01302020.NASDAQ_ITCH50.gz

4. This will run for a while and spit out files in the format of `output/<hour>.txt`
