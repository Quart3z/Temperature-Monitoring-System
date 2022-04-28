<template>
    <div id="training" class="p-3">
        <div class="">
            <div class="row mb-3">
                <div class="col-2">
                    <label for="seed">Seed</label>
                    <input type="number" class="form-control" id="seed" placeholder="Seed" v-model="hyperparameters.seed">
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label for="learningRate">Learning Rate</label>
                    <input type="number" class="form-control" id="learningRate" placeholder="Learning Rate" v-model="hyperparameters.learningRate">
                </div>
                <div class="col">
                    <label for="epochs">No. of epochs</label>
                    <input type="number" class="form-control" id="epochs" placeholder="No. of epochs" v-model="hyperparameters.nEpoch">
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <button class="btn btn-sm btn-primary mb-3" type="button" v-on:click="train" :disabled="!isTrainDone">
                        Train
                    </button>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <div class="spinner-border" role="status" v-if="!isTrainDone">
                    </div>
                    <div v-else>
                        <caption style="white-space: nowrap; overflow: hidden;">Evaluation</caption>
                        <table class="table table-sm">
                            <thead>
                                <tr>
                                    <th>Mean Squared Error</th>
                                    <th>Mean Absolute Error</th>
                                    <th>Root Mean Squared Error</th>
                                    <th>Relative Squared Error</th>
                                    <th>Pearson Correlation</th>
                                    <th>R^2 Coefficient of Determination</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td v-for="(r, index) in result" :key="index" scope="row">{{ r }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Training',
        data() {
            return {
                isTrainDone: true,
                hyperparameters: {
                    seed: 100,
                    learningRate: 0.001,
                    nEpoch: 3
                },
                datasets: [],
                result: [],
            }
        },
        props: {
            id: null
        },
        methods: {

            train: function () {

                this.isTrainDone = false;

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                        hyperparameters: this.hyperparameters
                    })
                };

                fetch("/train", request)
                    .then(response => {
                        if (response.status == 200) {
                            console.log(response)
                            this.isTrain = false

                            response.text().then(text => {

                                this.isTrainDone = true

                                let stats = text.split(/\s+/)
                                this.result = stats.slice(8, -1)
                            })

                        } else {
                            console.log(response)
                        }
                    })

            },

        },
        mounted() {},
        components: {},
    }
</script>