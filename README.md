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
- JAVA: 1.7+(recommendation version is 1.8)
- Apache Tomcat 7 (version 8 is not sure to be worked well...)

# Deployment
We assume that we have installed the JSP environment in our target host.

- target host: VPS (recommendation)
- clone the repo
- install the gradle (```sudo apt-get install gradle```) 
- using the command ```gradle tomcatRunWar``` to generate the ```acg-crawler.war```.i(The war file is in the /path/to/acg-crawler/build/libs)
- create the ```Auth.ini``` to set the SQLite databse path, Mailgun info and GMAIL info.
- export the runnable ```acg-crawler.jar```.
- Remember to copy the ```assets``` folder to the WEB-INF folder in WAR file.
- Remember to move the runnable jar file and ```auth.ini``` in the same directory path.
- set the crontab command: ```java -jar /path/to/acg-crawler.jar crawler``` to crawl data your specified time.
- set the crontab command: ```java -jar /path/to/acg-crawler.jar send-email``` to send newsletter every Thursday.
- set the crotab command: ```java -jar /path/to/acg-crawler.jar send-error-log``` to send the error log mail via Mailgun API.

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
[SQLite]
path=/path/to/anime.db
```

# Security
If you found some vulnerabilities about this web application project, please feel free to send the email to peter279k@gmail.com.

Thanks!
