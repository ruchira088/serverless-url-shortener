resource "aws_security_group" "lambda_security_group" {
  vpc_id = "${var.vpc_id}"

  ingress {
    from_port = 80
    protocol = "TCP"
    to_port = 80

    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 443
    protocol = "TCP"
    to_port = 443

    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 3306
    protocol = "TCP"
    to_port = 3306

    cidr_blocks = ["${data.aws_subnet.aws_data_subnets.*.cidr_block}"]
  }

  tags {
    Name = "${var.branch_name}-shortened-url-serverless-security-group"
  }
}

output "serverless_security_group" {
  value = "${aws_security_group.lambda_security_group.id}"
}

output "lambda_subnet_ids" {
  value = "${data.aws_subnet_ids.aws_lambda_subnet_ids.ids}"
}
