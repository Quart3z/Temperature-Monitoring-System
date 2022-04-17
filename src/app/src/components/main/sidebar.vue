<template>
    <div id="sidebar" class="position-fixed sidebar bg-dark p-3">
        <div class="text-center">
            <h1>[LOGO]</h1>
        </div>
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item"><a class="nav-link active">Dashboard</a></li>
            <li class="nav-item"><a class="nav-link text-white">Dataset</a></li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'Sidebar',
        data() {
            return {
                currentPage: "Dashboard"
            }
        },
        methods: {
            navigator: function () {

            }
        },
    }
</script>