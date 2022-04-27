<template>
    <div id="dashboard" class="">
        <Chart :id=id />
        <Prediction :id=id />
    </div>
</template>

<script>
    import Chart from "./chart/chart.vue"
    import Prediction from "./chart/prediction.vue"

    export default {
        name: 'Dashboard',
        data() {
            return {}
        },
        props: {
            id: null
        },
        components: {
            Chart,
            Prediction
        },
    }
</script>