# big-data-trend-visualizer

![Build Status](https://big-data-trends-visualizer-build-badges.s3.us-east-1.amazonaws.com/big-data-trends-visualizer.svg)

an implementation of using hadoop to process big data and visualize it in a web ui

![design](https://docs.google.com/drawings/d/e/2PACX-1vRg5tLiMZQMVOzlVNXohsZ4fYRICpZPz2uCeUSxUwCrHvEfHw_jsEnCTbVxwTIrlL2DLpt2rN6OAdJ5/pub?w=2077&h=1553)

## Demo

See [/web](/web)

## API
### Deploy
#### Setup
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

#### Run
1. `export AWS_PROFILE=big-data-trends-visualizer`
2. `mvn package && serverless deploy`

## Resources
- [Google Drive Assets](https://drive.google.com/open?id=1LiMbmS4jtuIU6pJq253PPsLLYdJVAwwY)
