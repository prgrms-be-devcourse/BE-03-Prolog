RUNNING_CONTAINER=$(docker ps | grep blue)
DEFAULT_CONF="nginx/default.conf"

if [ -z "$RUNNING_CONTAINER" ]; then
    TARGET_SERVICE="blue"
    OTHER_SERVICE="green"
else
    TARGET_SERVICE="green"
    OTHER_SERVICE="blue"
fi

echo "$TARGET_SERVICE Deploy..."
docker-compose pull $TARGET_SERVICE
docker-compose up -d $TARGET_SERVICE

# Wait for the target service to be healthy before proceeding
while true; do
    echo "$TARGET_SERVICE health check...."
    HEALTH=$(docker-compose exec nginx curl http://$TARGET_SERVICE:8080)
    if [ -n "$HEALTH" ]; then
        break
    fi
    sleep 3
done

# Update the nginx config and reload
sed -i "" "s/$OTHER_SERVICE/$TARGET_SERVICE/g" $DEFAULT_CONF
docker-compose exec nginx service nginx reload

# Stop the other service
docker-compose stop $OTHER_SERVICE
