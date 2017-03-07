# acg-crawler
A ACG crawler for crawling the ACG news!

# To do lists
~~- subscribe page~~

~~- unscribe page~~

~~- store subscribed email lists~~

~~- get subscribed email lists~~

- send email via GMAIL SMTP server

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
- target host: VPS (recommendation)
- clone the repo
- import the project to Eclipse IDE
- build the gradle to install the required libraries/pakcages.
- edit the ```java Auth.java``` to modify the GMAIL account and password.
- export the WAR file
- set the crontab for AcgCrawler to crawl data every your specified time.
- set the crontab for SendMail to send newsletter every week.
- enjoy it!

