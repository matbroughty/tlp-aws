#!/bin/bash
aws s3 cp ../../../target/tlp-aws-lambda-1.0.1.jar s3://timstwitterlisteningparty.com --grants read=uri=http://acs.amazonaws.com/groups/global/AllUsers &&
aws lambda update-function-code --function-name bluesky --s3-bucket timstwitterlisteningparty.com --s3-key tlp-aws-lambda-1.0.1.jar
