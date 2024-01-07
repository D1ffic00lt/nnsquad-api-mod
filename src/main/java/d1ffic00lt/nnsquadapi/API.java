package d1ffic00lt.nnsquadapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.command.ServerCommandSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@RestController
class API {
    private MinecraftServer server;

    @Autowired
    public void ApiController(MinecraftServer server) {
        this.server = server;
    }
    private static class PlayerList {
        public ArrayList<String> nicknames;
    }
    private PlayerList parseJson(String jsonString) {
        PlayerList playerList = new PlayerList();
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String nickname = jsonObject.get("nickname").getAsString();
            playerList.nicknames.add(nickname);
        }
        return playerList;
    }
    private boolean getStatus(String jsonString) {
        JsonObject jsonArray = JsonParser.parseString(jsonString).getAsJsonObject();
        return jsonArray.get("status").getAsBoolean();
    }
    @GetMapping("/api/v1/status")
    public CompletableFuture<ResponseEntity<String>> getStatus() {
        return getStatusAsync();
    }
    @GetMapping("/api/v1/bans")
    public CompletableFuture<ResponseEntity<String>> getBans() {
        return getBansAsync();
    }
    @GetMapping("/api/v1/players")
    public CompletableFuture<ResponseEntity<String>> getPlayers() {
        return getPlayersAsync();
    }
    @GetMapping("/api/v1/whitelist/list")
    public CompletableFuture<ResponseEntity<String>> getWhitelist() {
        return getWhitelistAsync();
    }

    @PostMapping("/api/v1/whitelist/remove")
    public CompletableFuture<ResponseEntity<String>> removeFromWhitelist(@RequestBody String requestData) {
        return removeFromWhitelistAsync(requestData);
    }

    @PostMapping("/api/v1/whitelist/add")
    public CompletableFuture<ResponseEntity<String>> addToWhitelist(@RequestBody String requestData) {
        return addToWhitelistAsync(requestData);
    }

    @PostMapping("/api/v1/whitelist/status")
    public CompletableFuture<ResponseEntity<String>> changeWhitelistStatus(@RequestBody String requestData) {
        return changeWhitelistStatusAsync(requestData);
    }

    @PostMapping("/api/v1/ban")
    public CompletableFuture<ResponseEntity<String>> banPlayer(@RequestBody String requestData) {
        return banPlayerAsync(requestData);
    }

    @PostMapping("/api/v1/UNban")
    public CompletableFuture<ResponseEntity<String>> unbanPlayer(@RequestBody String requestData) {
        return unbanPlayerAsync(requestData);
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> unbanPlayerAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
            ServerCommandSource source = server.getCommandSource().withSilent();
            try {
                commandDispatcher.execute(commandDispatcher.parse("command", source));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                PlayerList players = parseJson(data);
                for (String i : players.nicknames) {
                    commandDispatcher.execute(commandDispatcher.parse("pardon " + i, source));
                }
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body("{\"status\": error}");
            }
            return ResponseEntity.status(201).body("{\"status\": ok}");
        });
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> banPlayerAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
            ServerCommandSource source = server.getCommandSource().withSilent();
            try {
                commandDispatcher.execute(commandDispatcher.parse("command", source));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                PlayerList players = parseJson(data);
                for (String i : players.nicknames) {
                    commandDispatcher.execute(commandDispatcher.parse("ban " + i, source));
                }
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body("{\"status\": error}");
            }
            return ResponseEntity.status(201).body("{\"status\": ok}");
        });
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> changeWhitelistStatusAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
            ServerCommandSource source = server.getCommandSource().withSilent();
            try {
                commandDispatcher.execute(commandDispatcher.parse("command", source));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                boolean status = getStatus(data);
                if (status) {
                    commandDispatcher.execute(commandDispatcher.parse("whitelist on", source));
                }
                else {
                    commandDispatcher.execute(commandDispatcher.parse("whitelist off", source));
                }
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body("{\"status\": error}");
            }
            return ResponseEntity.status(201).body("{\"status\": ok}");
        });
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> addToWhitelistAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
            ServerCommandSource source = server.getCommandSource().withSilent();
            try {
                commandDispatcher.execute(commandDispatcher.parse("command", source));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                PlayerList players = parseJson(data);
                for (String i : players.nicknames) {
                    commandDispatcher.execute(commandDispatcher.parse("whitelist add " + i, source));
                }
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body("{\"status\": error}");
            }
            return ResponseEntity.status(201).body("{\"status\": ok}");
        });
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> removeFromWhitelistAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
            ServerCommandSource source = server.getCommandSource().withSilent();
            try {
                commandDispatcher.execute(commandDispatcher.parse("command", source));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                PlayerList players = parseJson(data);
                for (String i : players.nicknames) {
                    commandDispatcher.execute(commandDispatcher.parse("whitelist remove " + i, source));
                }
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body("{\"status\": error}");
            }
            return ResponseEntity.status(201).body("{\"status\": ok}");
        });
    }
    @Async
    public CompletableFuture<ResponseEntity<String>> getStatusAsync() {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(200).body("{\"status\": " + true + "}"));
    }
    @Async
    public CompletableFuture<ResponseEntity<String>> getBansAsync() {
        return CompletableFuture.supplyAsync(() -> {
            StringBuilder json = new StringBuilder("[");
            for (String i : server.getPlayerManager().getUserBanList().getNames()) {
                if (!json.toString().equals("[")) {
                    json.append(", ");
                }
                json.append(i);
            }
            json.append("]");
            return ResponseEntity.status(200).body(json.toString());
        });
    }
    @Async
    public CompletableFuture<ResponseEntity<String>> getPlayersAsync() {
        return CompletableFuture.supplyAsync(() -> {
            StringBuilder json = new StringBuilder("[");
            for (String i : server.getPlayerNames()) {
                if (!json.toString().equals("[")) {
                    json.append(", ");
                }
                json.append(i);
            }
            json.append("]");
            return ResponseEntity.status(200).body(json.toString());
        });
    }
    @Async
    public CompletableFuture<ResponseEntity<String>> getWhitelistAsync() {
        return CompletableFuture.supplyAsync(() -> {
            StringBuilder json = new StringBuilder("[");
            for (String i : server.getPlayerManager().getWhitelistedNames()) {
                if (!json.toString().equals("[")) {
                    json.append(", ");
                }
                json.append(i);
            }
            json.append("]");
            return ResponseEntity.status(200).body(json.toString());
        });
    }
}

//                } else if (request.startsWith("POST /api/v1/ban/")) {
//                    banPlayer(in, out);
//                } else if (request.startsWith("POST /api/v1/unban/")) {
//                    unbanPlayer(in, out);
//                } else {
//                    out.println("HTTP/1.1 404 Not Found");
//                    out.println();
//                }

//        private void banPlayer(BufferedReader in, PrintWriter out) throws CommandSyntaxException, IOException {
//            String line, data = "";
//            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
//            ServerCommandSource source = server.getCommandSource().withSilent();
//            commandDispatcher.execute(commandDispatcher.parse("command", source));
//

//            try {
//                PlayerList players = parseJson(data);
//                for (String i : players.nicknames) {
//                    commandDispatcher.execute(commandDispatcher.parse("ban " + i, source));
//                }
//            }
//            catch (Exception e) {
//                sendResponse(out, createJsonResponse("status", "error"), "400 OK");
//            }
//            finally {
//                sendResponse(out, createJsonResponse("status", "ok"), "201 OK");
//            }
//        }
//
//        private void unbanPlayer(BufferedReader in, PrintWriter out) throws CommandSyntaxException, IOException {
//            String line, data = "";
//            CommandDispatcher<ServerCommandSource> commandDispatcher = server.getCommandManager().getDispatcher();
//            ServerCommandSource source = server.getCommandSource().withSilent();
//            commandDispatcher.execute(commandDispatcher.parse("command", source));
//
//            while (!(line = in.readLine()).isEmpty()) {
//                if (line.startsWith("Content-Length: ")) {
//                    int contentLength = Integer.parseInt(line.substring(16));
//                    char[] content = new char[contentLength];
//                    in.read(content, 0, contentLength);
//                    data = new String(content);
//                    break;
//                }
//            }
//            try {
//                PlayerList players = parseJson(data);
//                for (String i : players.nicknames) {
//                    commandDispatcher.execute(commandDispatcher.parse("ban " + i, source));
//                }
//            }
//            catch (Exception e) {
//                sendResponse(out, createJsonResponse("status", "error"), "400 OK");
//            }
//            finally {
//                sendResponse(out, createJsonResponse("status", "ok"), "201 OK");
//            }
//        }
//        private String createJsonResponse(String key, String value) {
//            return "{\"" + key + "\": \"" + value + "\"}";
//        }
//
//        private void sendResponse(PrintWriter outputStream, String response, String status) throws IOException {
//            String httpResponse = "HTTP/1.1 " + status + "\r\n"
//                    + "Content-Type: application/json\r\n"
//                    + "\r\n"
//                    + response;
//            outputStream.println(httpResponse);
//        }
//
//        private PlayerList parseJson(String jsonString) {
//            PlayerList playerList = new PlayerList();
//            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
//                String nickname = jsonObject.get("nickname").getAsString();
//                playerList.nicknames.add(nickname);
//            }
//            return playerList;
//        }
//
//        private boolean getStatus(String jsonString) {
//            JsonObject jsonArray = JsonParser.parseString(jsonString).getAsJsonObject();
//            return jsonArray.get("status").getAsBoolean();
//        }
//    }
//}