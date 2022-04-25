<template>
    <div id="dataset" class="card bg-white m-3 p-3">
        <div class="card-title">
            <button v-on:click="train">Train</button>
            <a class="nav-item dropdown" data-bs-toggle="modal" data-bs-target="#upload-modal">
                <i class="bi-cloud-upload fs-4 px-2"></i>
            </a>
            <UploadModal :id=id />
        </div>
        <div class="card-body">
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Sensor</th>
                        <th scope="col"><input class="me-2" type="checkbox" v-model="columnCheck[0]">Timestamp</th>
                        <th scope="col"><input class="me-2" type="checkbox" v-model="columnCheck[1]">Humidity</th>
                        <th scope="col">Temperature</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="dataset in datasets" :key="dataset.id">
                        <th scope="row">{{ dataset.id }}</th>
                        <td>{{ dataset.device.uuid }}</td>
                        <td v-bind:class="{'table-secondary': columnCheck[0]}">{{ new Date(dataset.timestamp).toLocaleString() }}</td>
                        <td v-bind:class="{'table-secondary': columnCheck[1]}">
                            <input type="number" v-model="dataset.humidity" disabled>
                        </td>
                        <td>{{dataset.temperature}}</td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>
    import UploadModal from "../dashboard/chart/uploadModal.vue"

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
                            console.log(response)
                        } else {
                            console.log(response)
                        }
                    })

            }
        },
        mounted() {
            this.listData();
        },
        components: {
            UploadModal
        },
    }
</script>