#!/bin/bash

# 이미 설치되어 있지 않으면 설치
if [ ! -d "/usr/share/elasticsearch/plugins/analysis-nori" ]; then
  echo "Nori plugin not found, installing..."
  elasticsearch-plugin install analysis-nori --batch
else
  echo "Nori plugin already installed"
fi

# 원래 entrypoint 실행
exec /bin/tini -- /usr/local/bin/docker-entrypoint.sh "$@"
