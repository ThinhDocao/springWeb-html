# Cloudflare Tunnel Docker Setup

Project Spring Boot dang chay local port `3030`.

## 1. Tao tunnel tren Cloudflare

Vao Cloudflare Zero Trust Dashboard:

```text
Networks -> Tunnels -> Create a tunnel -> Cloudflared
```

Dat ten tunnel, vi du:

```text
aip-dma-demo
```

Chon Docker va copy token.

## 2. Cau hinh public hostname

Trong tunnel, them Public Hostname:

```text
Subdomain: demo
Domain: tenmiencuaban.com
Type: HTTP
URL: host.docker.internal:3030
```

Ket qua domain se la:

```text
https://demo.tenmiencuaban.com
```

Neu Spring Boot chay trong Docker cung network voi cloudflared, dung service name thay cho `host.docker.internal`, vi du:

```text
http://app:3030
```

## 3. Tao file token local

Copy file mau:

```powershell
Copy-Item .env.cloudflare.example .env.cloudflare
```

Sua `.env.cloudflare`:

```env
TUNNEL_TOKEN=token_cloudflare_cua_ban
```

Khong commit file `.env.cloudflare` vi no chua token that.

## 4. Chay Spring Boot

Dam bao app dang lang nghe o port `3030`:

```powershell
.\mvnw.cmd spring-boot:run
```

Kiem tra local:

```powershell
Test-NetConnection localhost -Port 3030
```

## 5. Chay tunnel

```powershell
docker compose -f docker-compose.cloudflare.yml --env-file .env.cloudflare up -d
```

Xem log:

```powershell
docker logs -f aip-dma-cloudflared
```

Dung tunnel:

```powershell
docker compose -f docker-compose.cloudflare.yml --env-file .env.cloudflare down
```

Khoi dong lai:

```powershell
docker compose -f docker-compose.cloudflare.yml --env-file .env.cloudflare restart
```
