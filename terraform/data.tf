data "aws_subnet_ids" "vpc_subnets" {
  vpc_id = "${var.vpc_id}"
}
