# big-data-trend-visualizer

an implementation of using hadoop to process big data and visualize it in a web ui. our first planned visualization is to use Nasdaq ITCH data transformed in VWAP. See 'Contribute to VWAP Visualization' below.

![Build Status](https://big-data-trends-visualizer-build-badges.s3.us-east-1.amazonaws.com/big-data-trends-visualizer.svg?cache=no)

## **Demo**: [/web](https://dclaze.github.io/big-data-trend-visualizer/web/home)


## Design
![design](https://docs.google.com/drawings/d/e/2PACX-1vRg5tLiMZQMVOzlVNXohsZ4fYRICpZPz2uCeUSxUwCrHvEfHw_jsEnCTbVxwTIrlL2DLpt2rN6OAdJ5/pub?w=2077&h=1553)

## API
### Deploy
> Prior to running deploy steps, please complete 'Prerequisites'

1. `cd api`
2. `export AWS_PROFILE=big-data-trends-visualizer`
3. `mvn package && serverless deploy`

#### Prerequisites
1. Install nvm, see https://github.com/nvm-sh/nvm
2. Install node, run `nvm use v12`
3. Install serverless, run `npm install -g serverless`
4. Install serverless plugins `npm install -g serverless-plugin-scripts`
5. Install Maven, see http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
6. Install AWS CLI, see https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html
  > eg. on Mac I ran `brew install awscli`

7. Run `aws configure`, and enter your AWS credentials. The `aws_access_key_id` and `aws_secret_access_key` are secret, reach out to get access.
  > Edit `~/.aws/credentials` and ensure the the profile is named `big-data-trends-visualizer`
  ```
   [big-data-trends-visualizer]
   aws_access_key_id = ********************
   aws_secret_access_key = ****************************************
   ```

## Resources
- [Google Drive Assets](https://drive.google.com/open?id=1LiMbmS4jtuIU6pJq253PPsLLYdJVAwwY)


## Web
The web front is composed of 2 views `web/home/index.html` and `web/visualizer/index.html`

### Run Locally
1. `cd web`
2. `npm install`
2. `npm start`
3. Open URL `http://127.0.0.1:1643/home/` in web browser

### Adding dependencies
1. `cd web`
2. `npm install`
3. `npx bower install <package-name>`, see https://bower.io/ for more
4. IMPORTANT: We are using github pages to host the webpage, please check in all `bower_components` files


## Contribute to VWAP Visualization
Our planned prototype will target a VWAP Visualization. The idea is to create a visualization for the [Nasdaq ITCH data](ftp://emi.nasdaq.com/ITCH/) and generate a [Volume Weighted Average Price](https://www.investopedia.com/terms/v/vwap.asp) visualization.

1. Add front end visualization to `web/visualizer/index.html`
  > Use D3, see https://d3js.org/, or other library

2. Split NASDAQ data
  > Determine the schema of the NASDAQ dataset, see S3 bucket, see https://console.aws.amazon.com/s3/buckets/nasdaq-itch/?region=us-east-1

3. Write Hadoop Map Reduce Algorithm
  > Transform from ITCH to VWAP on a cluster of Hadoop machines

4. Connect the input and output of Hadoop result to the API
  > Integrate our on-deman API with the Hadoop cluster. This requires us to deliver the input and output to the Hadoop cluster, store it somewhere to be retrieved, and wire it up to an API that returns the result to the customer
