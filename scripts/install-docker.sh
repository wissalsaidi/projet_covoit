#!/usr/bin/env bash
# Installe Docker Engine + plugin Compose sur Ubuntu (VMs Remote Labz du TP RT0910).
# Usage : sudo bash install-docker.sh
#   - ajoute l'utilisateur courant (et jenkins s'il existe) au groupe docker
#   - déclare le registre privé 10.11.14.241:5000 comme "insecure" (HTTP)
set -euo pipefail

REGISTRY="${REGISTRY:-10.11.14.241:5000}"
TARGET_USER="${SUDO_USER:-$USER}"

if [ "$(id -u)" -ne 0 ]; then
  echo "Lancer avec sudo : sudo bash $0" >&2
  exit 1
fi

echo "==> Suppression d'éventuels paquets Docker non officiels"
for p in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do
  apt-get remove -y "$p" >/dev/null 2>&1 || true
done

echo "==> Dépôt officiel Docker"
apt-get update
apt-get install -y ca-certificates curl
install -m 0755 -d /etc/apt/keyrings
if curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc; then
  chmod a+r /etc/apt/keyrings/docker.asc
  . /etc/os-release
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu ${UBUNTU_CODENAME:-$VERSION_CODENAME} stable" \
    > /etc/apt/sources.list.d/docker.list
  apt-get update
  apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
else
  echo "!! download.docker.com injoignable : repli sur les paquets Ubuntu"
  apt-get install -y docker.io docker-compose-v2 || apt-get install -y docker.io docker-compose-plugin
fi

echo "==> Registre privé autorisé en HTTP : $REGISTRY"
mkdir -p /etc/docker
cat > /etc/docker/daemon.json <<JSON
{
  "insecure-registries": ["$REGISTRY"],
  "log-driver": "json-file",
  "log-opts": { "max-size": "10m", "max-file": "3" }
}
JSON

systemctl enable --now docker
systemctl restart docker

echo "==> Groupe docker"
usermod -aG docker "$TARGET_USER"
if id jenkins >/dev/null 2>&1; then
  usermod -aG docker jenkins
  systemctl restart jenkins 2>/dev/null || true
fi

echo "==> Vérification"
docker --version
docker compose version
docker run --rm hello-world | grep -m1 "Hello from Docker"
docker info 2>/dev/null | grep -A1 "Insecure Registries"
echo
echo "OK. Déconnecte-toi / reconnecte-toi (ou 'newgrp docker') pour utiliser docker sans sudo en tant que $TARGET_USER."
