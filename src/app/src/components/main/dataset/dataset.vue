<template>
    <div id="dataset" class="card bg-white m-3 p-3">
        <div class="card-title">
            <button>Train</button>
        </div>
        <div class="card-body">
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col"><input class="me-2" type="checkbox" v-model="columnCheck[0]">Sensor</th>
                        <th scope="col"><input class="me-2" type="checkbox" v-model="columnCheck[1]">Timestamp</th>
                        <th scope="col"><input class="me-2" type="checkbox" v-model="columnCheck[2]">Temperature</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="dataset in datasets" :key="dataset.id">
                        <th scope="row">{{ dataset.id }}</th>
                        <td v-bind:class="{'table-secondary': columnCheck[0]}">{{ dataset.device.uuid }}</td>
                        <td v-bind:class="{'table-secondary': columnCheck[1]}">{{ new Date(dataset.timestamp).toLocaleString() }}</td>
                        <td v-bind:class="{'table-secondary': columnCheck[2]}">{{ dataset.temperature }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'Dataset',
        data() {
            return {
                datasets: [],
                columnCheck: [false, false, false, false]
            }
        },
        props: {
            id: null
        },
        methods: {
            listData: function () {

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                        filter: {
                            count: 20,
                        }
                    })
                };

                fetch("/listData", request)
                    .then(response => {
                        if (response.status == 200) {
                            response.json().then(entries => {

                                this.datasets = entries.content.slice()

                            })

                        } else {
                            console.log(response)
                        }
                    })

            },
            train: function () {
                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                        filter: {
                            count: 20,
                        }
                    })
                };

                fetch("/train", request)
                    .then(response => {
                        if (response.status == 200) {
                            response.json().then(entries => {

                                this.datasets = entries.content.slice()

                            })

                        } else {
                            console.log(response)
                        }
                    })
            }
        },
        mounted() {
            this.listData();
        },
        components: {},
    }
</script>