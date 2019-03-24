
data "aws_subnet_ids" "aws_data_subnet_ids" {
  vpc_id = "${var.vpc_id}"

  tags {
    Type = "data"
  }
}

data "aws_subnet_ids" "aws_lambda_subnet_ids" {
  vpc_id = "${var.vpc_id}"

  tags {
    Type = "lambda"
  }
}

data "aws_subnet" "aws_data_subnets" {
  count = "${length(data.aws_subnet_ids.aws_data_subnet_ids.ids)}"
  id = "${data.aws_subnet_ids.aws_data_subnet_ids.ids[count.index]}"
}

output "cidr_blocks" {
  value = "${data.aws_subnet.aws_data_subnets.*.cidr_block}"
}
