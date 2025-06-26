### WiamSpammer Mod - AI Generated English Usage Tutorial

#### 🔧 **Installation**  
1. Place the mod JAR in your Minecraft `mods` folder  
2. Requires **Fabric API** and **Fabric Language Kotlin** (if used)  

---

#### 💬 **Spammer Commands**  
*Start spamming messages:*  
```bash
/spammer <delay_seconds> <message>
```  
- `delay_seconds`: Delay between messages (e.g., `0.5` = half-second)  
- `message`: Text or command to spam (prefix commands with `/`)  

*Example:*  
```bash
/spammer 1.0 Hello world!
```  
➔ Sends "Hello world!" every second.  

*Stop spamming:*  
```bash
/spammer stop
```  

---

#### 🤖 **Auto-Responder Configuration**  
*Reload config file:*  
```bash
/loadspammerconfig
```  
*(Edits require reloading)*  

**Config File Location:**  
`/.minecraft/config/wiamspammer.json`  

---

#### ⚙️ **Auto-Responder Rules Setup**  
Edit `wiamspammer.json` to add rules like:  
```json
{
  "canAutoMessage": true,
  "entryList": [
    {
      "isThisOn": false,
      "isPositiveMatch": true,
      "isMatchSystemMessage": false,
      "isMatchChatMessage": true,
      "isMatchDmMessage": false,
      "pattern": "^(\\d{4})-(\\d{2})-(\\d{2})$",
      "matchPlayers": [],
      "message": "Y:${0}, D:${1}, H:${2}",
      "sendDelay": 0.0
    },
    {
      "isThisOn": false,
      "isPositiveMatch": true,
      "isMatchSystemMessage": false,
      "isMatchChatMessage": true,
      "isMatchDmMessage": false,
      "pattern": "123123123",
      "matchPlayers": [],
      "message": "abbabbabba",
      "sendDelay": 0.0
    },
    {
      "isThisOn": true,
      "isPositiveMatch": true,
      "isMatchSystemMessage": true,
      "isMatchChatMessage": false,
      "isMatchDmMessage": false,
      "pattern": "GetTossed7679 joined the game",
      "matchPlayers": [],
      "message": "Hello!! Greet-phobie",
      "sendDelay": 0.0
    },
    {
      "isThisOn": true,
      "isPositiveMatch": true,
      "isMatchSystemMessage": true,
      "isMatchChatMessage": false,
      "isMatchDmMessage": false,
      "pattern": "(?!Wiamotit1e\\b)(?!GetTossed7679\\b)(?!.*[<>])(\\S+) joined the game$",
      "matchPlayers": [],
      "message": "Welcome!! ${0}",
      "sendDelay": 0.0
    }
  ]
}
```  

##### 🔍 **Rule Parameters:**  
| Parameter              | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| `isThisOn`             | Enable/disable this rule                                                    |
| `isPositiveMatch`      | `true` = trigger on match, `false` = trigger on no match                   |
| `isMatchSystemMessage` | Respond to system messages (e.g., achievements)                             |
| `isMatchChatMessage`   | Respond to public chat messages                                            |
| `isMatchDmMessage`     | Respond to private messages (DMs)                                          |
| `matchPlayers`         | Only respond to these players (Not work on certain servers like pure vanilla  |
| `pattern`              | Regex pattern to match in messages (e.g., `"Hello (.*)!"`)                 |
| `message`              | Response text (use `${0}`, `${1}`, etc. for regex capture groups, **The 0th index that included all caputer groups is jumped, now the 0th index is the 1st regex capture group, the 1st index is the 2nd and etc.**)          |
| `sendDelay`            | Delay (seconds) before sending response                                    |

---

#### 🧩 **Key Features**  
1. **Regex Support**  
   - Use `(.*)` to capture text, reference with `${0}` in responses  
   - *Example:* Pattern `"v(\d+)\.(\d+)"` → Response `"Latest: ${0}"`  

2. **Command Integration**  
   - Start commands with `/` in messages:  
   ```bash
   /spammer 2.0 /home
   ```  
   ➔ Executes `/home` every 2 seconds  

3. **Disconnect Safety**  
   - Spammer/auto-messages stop automatically when disconnected  

4. **Message Delay Queue**  
   - Messages with delays are queued (e.g., `sendDelay: 5.0` = wait 5 seconds)  

---

#### ❓ **Troubleshooting**  
- **Commands not working?** Ensure you're on a server with cheats enabled.  
- **Config not loading?** Check `wiamspammer.json` for syntax errors.  
- **Regex issues?** Test patterns at [regex101.com](https://regex101.com).  

> ⚠️ **Warning:** Excessive spamming may result in bans. Use responsibly!
