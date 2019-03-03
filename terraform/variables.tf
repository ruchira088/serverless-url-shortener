variable "vpc_id" {
  type = "string"
  default = "vpc-0de0b71172da2ed54"
}

variable "subnet_ids" {
  type = "list"
  default = ["subnet-097e8d6c2773f02a0", "subnet-0a79d0b2695874ea6"]
}
