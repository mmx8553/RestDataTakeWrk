package EmptyBot;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.LongPollingBot;



public abstract class MxTelegramLongPollingBot extends DefaultAbsSender implements LongPollingBot {
    public MxTelegramLongPollingBot() {
        this((DefaultBotOptions) ApiContext.getInstance(DefaultBotOptions.class));
    }

    public MxTelegramLongPollingBot(DefaultBotOptions options) {
        super(options);
    }

    public void clearWebhook() throws TelegramApiRequestException {

        /*
        try {
            CloseableHttpClient httpclient = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            Throwable var2 = null;

            try {
                String url = this.getOptions().getBaseUrl() + this.getBotToken() + "/" + "setwebhook";
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(this.getOptions().getRequestConfig());
                CloseableHttpResponse response = httpclient.execute(httpGet);
                Throwable var6 = null;

                try {
                    HttpEntity ht = response.getEntity();
                    BufferedHttpEntity buf = new BufferedHttpEntity(ht);
                    String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(responseContent);
                    if(!jsonObject.getBoolean("ok")) {
                        throw new TelegramApiRequestException("Error removing old webhook", jsonObject);
                    }
                } catch (Throwable var36) {
                    var6 = var36;
                    throw var36;
                } finally {
                    if(response != null) {
                        if(var6 != null) {
                            try {
                                response.close();
                            } catch (Throwable var35) {
                                var6.addSuppressed(var35);
                            }
                        } else {
                            response.close();
                        }
                    }

                }
            } catch (Throwable var38) {
                var2 = var38;
                throw var38;
            } finally {
                if(httpclient != null) {
                    if(var2 != null) {
                        try {
                            httpclient.close();
                        } catch (Throwable var34) {
                            var2.addSuppressed(var34);
                        }
                    } else {
                        httpclient.close();
                    }
                }

            }

        } catch (JSONException var40) {
            throw new TelegramApiRequestException("Error deserializing setWebhook method response", var40);
        } catch (IOException var41) {
            throw new TelegramApiRequestException("Error executing setWebook method", var41);
        }
        */
    }

    public void onClosing() {
        this.exe.shutdown();
    }
}
