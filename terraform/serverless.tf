resource "aws_security_group" "serverless_security" {
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

    cidr_blocks = ["${aws_security_group.database_security_group.id}"]
  }

  tags {
    Name = "shortened-url-serverless-security-group"
  }
}

output "serverless_security_group" {
  value = "${aws_security_group.serverless_security.id}"
}
