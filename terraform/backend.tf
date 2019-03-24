terraform {
  backend "s3" {
    region = "ap-southeast-2"
    bucket = "terraform.ruchij.com"
    key = "serverless-deploy-url-shortener.tfstate"
  }
}
