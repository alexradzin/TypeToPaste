Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = "${user.home}\Desktop\TypeToPaste.LNK"
Set oLink = oWS.CreateShortcut(sLinkFile)
   oLink.TargetPath = "${java.home}\bin\javaw"
   oLink.Arguments = "${typetopaste.args}"
   oLink.Description = "Type To Paste"   
   oLink.HotKey = "${typetopaste.shortcut}"
   oLink.IconLocation = "${typetopaste.icon}"
oLink.Save

