#!/usr/bin/env bash
# Build and push whisperai image
# AWS CLI to must be configured to the correct region
# ECR permissions must be set to allow upload (https://stackoverflow.com/a/72621029)
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 537408061242.dkr.ecr.us-east-2.amazonaws.com
docker build -t mere-recipes .
docker tag mere-recipes:whisperai 537408061242.dkr.ecr.us-east-2.amazonaws.com/mere-recipes:whisperai
docker push 537408061242.dkr.ecr.us-east-2.amazonaws.com/mere-recipes:whisperai