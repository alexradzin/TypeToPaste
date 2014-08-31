Abstract
========

Desktop Java App that helps user to paste text when paste is forbidden or unsupported

Motivation
==========

Some application do not allow user to perfrom paste operation from clipboard. Typical examples are;
* password/email/username fields
* target machine when working with remote desktop. See: [Device and Resource Redirection](http://technet.microsoft.com/en-us/library/ee791794(WS.10).aspx)


How does it work
================

If application does not allow paste operation but allow typing we can emulate the user's typing events and "paste" text character-by-character. 
This is exactly what TypeToPaste does. 



Name derivation
===============

TypeToPaste's name derivation is in its mechanism. It types text instead of pasting it from clipboard. 


Installation
============

TypeToPaste is a java application distributed as a single jar file that can be downloaded from [here](https://sites.google.com/site/typetopaste/download/typetopaste.jar).
It can be installed automatically. Click [here](https://sites.google.com/site/typetopaste/download/typetopaste.jnlp) to install it automatically using Java Web Start. 
Although the application is written in java and therefore is cross-platform by definition its set-up code uses platform specific features and currently is fully supported on MS Windows only. 


Recommended usage
=================

On MS Windows
-------------

Install TypeToPaste using [java web start](https://sites.google.com/site/typetopaste/download/typetopaste.jnlp?attredirects=0&d=1). TypeToPaste shortcut will appear on your desktop. 

On other platforms
------------------

Download jar from [here](https://sites.google.com/site/typetopaste/download/typetopaste.jar) and define custom shortcut using tools available on your platform. 
  

For all users
-------------

When you have to copy and paste text to application that does not support paste do the following. 
1. Select and copy the text to clipboard
2. Press keyboard shortcut that you defined for TypeToPaste (Ctrl+Alt+C by default). The half-transparent window will appear under your mouse pointer. 
3. Move mouse to position where you have to paste the text and click left mouse button. 

 
For more information refer to the [TypeToPaste home page](https://sites.google.com/site/typetopaste/).