# Good first issue bot

[![build and test](https://github.com/agsamkin/goodfirstissue-bot/actions/workflows/build.yml/badge.svg)](https://github.com/agsamkin/goodfirstissue-bot/actions/workflows/build.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/3f81753ef428bf5a7464/maintainability)](https://codeclimate.com/github/agsamkin/goodfirstissue-bot/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/3f81753ef428bf5a7464/test_coverage)](https://codeclimate.com/github/agsamkin/goodfirstissue-bot/test_coverage)

Telegram bot for search "good first issue" on GitHub.

Good First Issue bot curates easy pickings from popular open-source projects, and helps you make your first contribution to open-source.

### Development

Telegram:
* Create new telegram bot with [@BotFather](https://t.me/BotFather)
* Set env TG_BOT_USERNAME, TG_BOT_TOKEN

Webhooks:
* Install [ngrok](https://ngrok.com/download) for Windows or [localtunnel](https://theboroer.github.io/localtunnel-www/) for Linux
* Get webhook with ngrok (or localtunnel)
* Set webhook with: 
```
https://api.telegram.org/bot{my_bot_token}/setWebhook?url={url_to_send_updates_to}
```

Install RabbitMQ with Docker:
```
docker pull rabbitmq:3.11.0-management
docker volume create rabbitmq_data
docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq_data:/var/lib/rabbitmq --restart=unless-stopped rabbitmq:3.11.0-management
```

GitHub:
* Create new [GitHub token](https://github.com/settings/tokens)
* Set env GITHUB_TOKEN