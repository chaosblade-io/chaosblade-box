#!/usr/bin/expect

#set timeout 60

set host [lindex $argv 0]
set username [lindex $argv 1]
set password [lindex $argv 2]
set port [lindex $argv 3]

cd ~/.ssh
spawn ssh-copy-id -i id_rsa.pub -p $port $username@$host
expect {
 "(yes/no)?"
  {
    send "yes\n"
    expect "*password:" { send "$password\n"}
  }
  "Are you sure you want to continue connecting" {
    send "yes\n"
    expect "*password:" { send "$password\n"}
  }
 "*password:"
  {
    send "$password\n"
  }
}
interact
