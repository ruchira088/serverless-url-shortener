
resource "random_string" "database_username" {
  length = 16
  number = false
  special = false
}

resource "random_string" "database_password" {
  length = 32
  special = false
}

resource "aws_rds_cluster" "database_cluster" {
  cluster_identifier = "${var.branch_name}-shortened-url"
  engine = "aurora"
  engine_mode = "serverless"
  database_name = "shortenedUrls"
  master_username = "${random_string.database_username.result}"
  master_password = "${random_string.database_password.result}"
  kms_key_id = "${aws_kms_key.kms_key.arn}"
  db_subnet_group_name = "${aws_db_subnet_group.database_subnet_group.name}"
  vpc_security_group_ids = ["${aws_security_group.database_security_group.id}"]

  tags {
    Name = "${var.branch_name}-shortened-url"
  }
}

resource "aws_security_group" "database_security_group" {
  vpc_id = "${var.vpc_id}"

  ingress {
    from_port = 3306
    protocol = "TCP"
    to_port = 3306

    security_groups = ["${aws_security_group.lambda_security_group.id}"]
  }

  tags {
    Name = "${var.branch_name}-shortened-url-database-security-group"
  }
}

resource "aws_db_subnet_group" "database_subnet_group" {
  subnet_ids = ["${data.aws_subnet_ids.aws_data_subnet_ids.ids}"]

  tags {
    Name = "${var.branch_name}-shortened-url-database-subnet-group"
  }
}

resource "aws_kms_key" "kms_key" {
  description = "KMS key for the shortened URL database"

  tags {
    Name = "${var.branch_name}-shortened-url-database-kms-key"
  }
}

output "database_name" {
  value = "${aws_rds_cluster.database_cluster.database_name}"
}

output "database_endpoint" {
  value = "${aws_rds_cluster.database_cluster.endpoint}"
}

output "database_username" {
  value = "${random_string.database_username.result}"
}

output "database_password" {
  value = "${random_string.database_password.result}"
}
