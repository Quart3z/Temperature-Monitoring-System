<template>
    <div id="chart" class="card bg-white m-3 p-3">
        <div class="card-title">
            <button class="btn btn-sm btn-primary mb-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                Filter
            </button>
            <div class="collapse" id="collapseExample">
                <div class="">
                    <div class="d-flex flex-column mb-2">
                        <label class="form-label">Time period</label>
                        <datepicker v-model="period" range></datepicker>
                    </div>
                    <!-- <div class="d-flex"> -->
                    <div class="d-flex flex-column mb-2">
                        <label class="form-label">No. of nodes - {{ count }}</label>
                        <input type="range" class="form-range w-50 me-2" v-model="count" v-on:change="getData">
                    </div>
                    <div class="d-flex flex-column mb-2">
                        <label class="form-label">Temperature - {{ minReading}}</label>
                        <input type="range" class="form-range w-50 me-2" v-model="minReading" v-bind:max="maxReading" v-on:change="getData">
                    </div>
                    <!-- </div> -->
                    <hr>
                </div>
            </div>
        </div>
        <div class="card-body">
            <highcharts :options="chartConfig" v-if="Object.entries(datasets).length" :key="redraw" />
            <div v-else>
                [It's quite empty right now]
            </div>
        </div>
    </div>
</template>

<script>
    import {
        Chart
    } from 'highcharts-vue'
    import DatePicker from 'vue2-datepicker';
    import 'vue2-datepicker/index.css';


    export default {
        name: 'Chart',
        data() {
            return {
                redraw: true,
                count: 2,
                minReading: 10,
                maxReading: 50,
                period: [],
                datasets: [],
                chartConfig: {
                    title: {
                        text: "Devices Temperature"
                    },
                    yAxis: {
                        type: 'float',
                        title: {
                            text: "Temperature",
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        title: {
                            text: "Time"
                        }
                    },
                    scrollbar: {
                        enabled: true
                    },
                }
            }
        },
        props: {
            id: null
        },
        methods: {

            getData: function () {

                this.period.forEach(function(part, index) {
                    return Math.floor(new Date(this[index]).getTime() / 1000)
                }, this.period)

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                        filter: {
                            count: this.count,
                            period: this.period,
                            temperature: []
                        }
                    })
                };

                fetch("/getData", request)
                    .then(response => {
                        if (response.status == 200) {
                            response.json().then(entries => {

                                this.datasets = []

                                for (let entry in entries) {
                                    this.datasets.push({

                                        name: "Device " + entry,
                                        data: entries[entry].map(e => {
                                            return [e.timestamp, e.temperature]
                                        })

                                    })
                                }

                                this.chartConfig.series = this.datasets;
                                this.redraw = !this.redraw
                            })

                        } else {
                            console.log(response)
                        }
                    })

            },
            exportData: function () {
                // this.getCSV();
            }

        },
        mounted() {

            this.getData();

        },
        components: {
            highcharts: Chart,
            datepicker: DatePicker
        },
    }
</script>