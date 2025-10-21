#!/usr/bin/expect
# Copyright 2025 The ChaosBlade Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


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
