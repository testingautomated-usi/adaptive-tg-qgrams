from config import PHOENIX_NAME
from executors.executor import Executor

from global_log import GlobalLog


class PhoenixExecutor(Executor):

    def __init__(self):
        super().__init__(app_name=PHOENIX_NAME)
        self.logger = GlobalLog("PhoenixExecutor")

    def _start_application_container(
        self, network_name: str, container_name: str
    ) -> None:

        self.logger.debug(f"Starting container {container_name}")

        healthcheck = {
            "test": "curl -f http://localhost:4000",
            "interval": 1 * 10**9,
            "timeout": 5 * 10**9,
            "retries": 20,
            "start_period": 5 * 10**9,
        }

        self.client.containers.run(
            image="dockercontainervm/phoenix-trello:latest",
            auto_remove=True,
            detach=True,
            tty=True,
            stdin_open=True,
            name=container_name,
            entrypoint="./run-services-docker.sh",
            environment=[
                "PATH=/root/.kiex/elixirs/elixir-1.3.1/bin:/root/.kiex/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            working_dir="/home/phoenix-trello",
            healthcheck=healthcheck,
            command="bash",
        )

        self.client.networks.get(network_name).connect(
            container=container_name, aliases=[self.app_name]
        )
