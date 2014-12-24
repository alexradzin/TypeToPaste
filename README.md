Abstract
========

Desktop Java App that helps user to paste text when paste is forbidden or unsupported

Motivation
==========

Some application do not allow user to perform paste operation from clipboard. Typical examples are:
* password/email/username fields
* target machine when working with remote desktop. See: [Device and Resource Redirection](http://technet.microsoft.com/en-us/library/ee791794(WS.10).aspx)


Name derivation
===============

Name "TypeToPaste" explains how the program works. It types text instead of pasting it from clipboard. 


How does it work
================

If application does not allow paste operation but allow typing we can emulate the user's typing events and "paste" text character-by-character. 
This is exactly what TypeToPaste does.


Unicode
-------

It is easy to type ASCII characters. However it is more complicated with unicode characters. TypeToPaste does not print characters. It simulates keystrokes. 
This means that is must know the keyboard layout of target machine. For example if user wants to paste text from his computer to another machine using remote 
desktop TypeToPaste must know keyboard layout of remote machine that is probably possible but very complicated. Other strategy is to type the code of character. 
Different platforms provide slightly different implementation of this feature. On Windows you can press Alt button, then type code of needed character using the 
numbers keyboard, then release Alt button. On Linux user has to press Ctrl-Shift-U, then release U, type character code and release Crtl-Shift. The method depends
on target platform that can be different from platform where TypeToPaste is running. So, user can change method manually by pressing Alt-A or Alt-U. 

    
Typing velocity
---------------

Typically there are no issues with typing velocity. TypeToPaste emulates keystrokes as quickly as it can and everything works well. However in some environments
(e.g. when using [Citrix](http://www.citrix.com/) based solution) some characters are lost. It seems that the issue is with some kind of network latency. 
The solution is to type lower, i.e. add delay between each character. By default we use 1 ms delay displayed on the top of the TypeToPaste window. This delay 
can be changed using Alt+↑ and Alt+↓. 





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
3. Move mouse to position where you have to paste the text and click left mouse button. Alternatively you can use  ←↑→↓ to locate the pasting position and Ctrl-V or Shift-Insert to perform paste.
4. Use Alt+↑ and Alt+↓ to ajust typing dealy if needed.
5. Use Alt-A or Alt-U to set method of typing Unicode characters.  


![help text](../../blob/master/src/main/resources/clickpadhelp.png "Usage")
 
For more information refer to the [TypeToPaste home page](https://sites.google.com/site/typetopaste/).