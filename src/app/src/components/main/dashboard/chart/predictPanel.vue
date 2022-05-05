<template>
    <div id="predict" class="p-3">
        <div class="row mb-3">
            <div class="col d-flex flex-column">
                <label class="form-label">Timestamp</label>
                <DatePicker v-model="timestamp" type="datetime" range :clearable="false" />
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-2">
                <button class="btn btn-sm btn-primary mb-3" type="button" v-on:click="predict" :disabled="!isPredictDone">
                    Predict
                </button>
                <!-- <label for="seed">Seed</label> -->
                <!-- <input type="number" class="form-control" id="seed" placeholder="Seed" v-model="hyperparameters.seed"> -->
            </div>
        </div>
        <highcharts :options="chartConfig" :key="redraw" />
    </div>
</template>

<script>
    // Highchart
    import {
        Chart
    } from 'highcharts-vue'
    import highcharts from 'highcharts'
    import Exporting from 'highcharts/modules/exporting'
    import ExportData from 'highcharts/modules/export-data'

    // Date picker
    import DatePicker from 'vue2-datepicker';
    import 'vue2-datepicker/index.css';


    Exporting(highcharts)
    ExportData(highcharts)

    export default {
        name: 'Predicting',
        data() {
            return {
                redraw: true,
                isPredictDone: true,
                results: [],
                timestamp: "",
                chartConfig: {
                    time: {
                        useUTC: false
                    },
                    title: {
                        text: "Predicted Readings"
                    },
                    chart: {
                        spacingLeft: 30,
                        spacingRight: 30,
                    },
                    series: [{
                        name: "Predictions",
                        data: []
                    }],
                    yAxis: {
                        type: 'float',
                        title: {
                            text: "Temperature",
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        title: {
                            text: "Timestamp"
                        }
                    },
                    legend: {
                        margin: 30,
                        align: 'right',
                        layout: "vertical",
                        verticalAlign: 'middle',
                        floating: false
                    },
                    exporting: {
                        enabled: true,
                        csv: {
                            decimalPoint: '.',
                            dateFormat: '%Y-%m-%d %H:%M:%S'
                        },
                        buttons: {
                            contextButton: {
                                menuItems: [
                                    'printChart',
                                    'downloadPNG',
                                    'downloadJPEG',
                                    'downloadPDF',
                                    'downloadSVG',
                                    'downloadCSV',
                                    'downloadJSON'
                                ]
                            }
                        }
                    },
                }
            }
        },
        props: {
            id: null
        },
        methods: {

            predict: function () {


                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                    })
                };

                fetch("/predict", request)
                    .then(response => {
                        if (response.status == 200) {
                            console.log(response)

                            response.text().then(text => {

                                // this.isTrainDone = true

                                // let stats = text.split(/\s+/)
                                // this.result = stats.slice(8, -1)


                                this.chartConfig.series[0].data = JSON.parse(text);

                            })
                            // const reader = response.body.getReader();


                            // return new ReadableStream({
                            //     start(controller) {
                            //         return pump;

                            //         function pump() {
                            //             return reader.read().then(({
                            //                 done,
                            //                 value
                            //             }) => {
                            //                 if (done) {
                            //                     controller.close()
                            //                     return;
                            //                 }
                            //                 controller.enqueue(value)
                            //                 return pump;
                            //             })
                            //         }
                            //     }
                            // })


                        } else {
                            console.log(response)
                        }
                    })
                    .then(stream => {
                        new Response(stream)
                    })
                    .then(response => {
                        response.blob()
                    })
                    .then(blob => {
                        URL.createObjectURL(blob)
                    })
                    .then(url => {
                        console.log(url)
                    })
                    .catch(err => {
                        console.log(err)
                    })

            },

        },
        mounted() {},
        components: {
            highcharts: Chart,
            DatePicker
        },
    }
</script>