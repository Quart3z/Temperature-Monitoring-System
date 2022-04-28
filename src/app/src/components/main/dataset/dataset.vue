<template>
    <div id="dataset" class="card bg-white m-3 p-3">
        <div class="card-title">
            <a class="nav-item" v-on:click="train">
                <i class="bi bi-cpu fs-4 px-2"></i>
            </a>
            <a class="nav-item" data-bs-toggle="modal" data-bs-target="#upload-modal">
                <i class="bi bi-cloud-arrow-up fs-4 px-2"></i>
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
                        <th scope="col">Temperature</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="dataset in datasets" :key="dataset.id">
                        <th scope="row">{{ dataset.id }}</th>
                        <td class="d-inline-block text-truncate" style="width: 150px">{{ dataset.device.uuid }}</td>
                        <td v-bind:class="{'table-secondary': columnCheck[0]}">{{ new Date(dataset.timestamp).toLocaleString() }}</td>
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
                columnCheck: [false, false, false, false],
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

            },

            edit: function(){

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