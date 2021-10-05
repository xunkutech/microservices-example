FROM maven:3.8.2-adoptopenjdk-8

RUN \
    set -eux \
    && apt-get update \
    && apt-get -y upgrade \
    && apt-get -y install --no-install-recommends \
        apt-transport-https \
        ca-certificates \
        gnupg \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg |gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu focal stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt-get update \
    && apt-get  -y install --no-install-recommends docker-ce-cli \
    # Clean up
    && apt-get -y autoremove \
    && apt-get -y clean \
    && rm -rf /var/lib/apt/lists/*

# Setup language
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8

    