# acg-crawler
A ACG crawler for crawling the ACG news!

# To do lists
~~- subscribe page~~

~~- unscribe page~~

~~- store subscribed email lists~~

~~- get subscribed email lists~~

- send email via GMAIL SMTP server (not yet...)

~~- send email via MailGun API~~

~~- send log email via MailGun API~~

- (enhancement) crawl more resources

- finish the Deployment section

~~- (security) add CSRF-token for every pages
(Including AnimeNews, AnimeHotNews, unscribe/subscribe email address)~~

~~- (security) user input validation (email)~~

# Requirement
- Operating System: Ubuntu/16.04 (The Ubuntu 14.04 is not available for this project.)
- Apache Tomcat version: 7
- JAVA: 1.7+(recommendation version is 1.8)
- Apache Tomcat 7 (version 8 is not sure to be worked well...)

# Deployment (Manual approach)
We assume that we have installed the JSP environment in our target host.

- target host: VPS (recommendation)
- clone the repo
- install the gradle (```sudo apt-get install gradle```) 
- using the command ```gradle tomcatRunWar``` to generate the ```acg-crawler.war```.i(The war file is in the /path/to/acg-crawler/build/libs)
- create the ```Auth.ini``` to set the Mailgun info and GMAIL info.
- export the runnable ```acg-crawler.jar```.
- Remember to copy the ```assets``` folder to the WEB-INF folder in WAR file.
- Remember to move the runnable jar file and ```auth.ini``` in the same directory path.
- set the crontab command: ```java -jar /path/to/acg-crawler.jar ``` to crawl data, send email and send error log mail.
- enjoy it!

# auth.ini
## The sample auth.ini file contents are as follows:

```
[MAILGUN]
api-key=key-XXXXXXXXXXX
domain-name=peter279k.com.tw
api-base-url=https://api.mailgun.net/v3/peter279k.com.tw/messages
from-email-address=peter279k@gmail.com
from-email-account=AnimeNews <admin@peter279k.com.tw>
[GMAIL]
account=your-gmail-addresss
password=your-gmail-password
```
# SETUP.sh (Automatic deployment)
```bash
#!/bin/bash

echo "This project is available for the Ubuntu 16.04LTS"

sudo apt-get install gradle git-core
sudo apt-get install default-jdk default-jre tomcat7

git clone https://github.com/peter279k/acg-crawler.git
cd acg-crawler

gradle jar --info
gradle war --info



```

# Security
If you found some vulnerabilities about this web application project, please feel free to send the email to peter279k@gmail.com.

Thanks!
