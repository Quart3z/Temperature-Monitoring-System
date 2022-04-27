<template>
    <div id="header" class="navbar navbar-expand-lg bg-white flex-column border-bottom">
        <div class="container-fluid">
            <h1 class="navbar-brand text-primary">Temps Monitor</h1>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="modal" data-bs-target="#upload-modal">
                        <i class="bi bi-cloud-arrow-up fs-4 px-2"></i>
                    </a>
                </li>
                <UploadModal :id=id />
                <li class="nav-item">
                    <a class="nav-link dropdown-toggle" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person fs-4 px-2"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark" aria-labelledby="navbarDarkDropdownMenuLink">
                        <li><a class="dropdown-item" href="">Profile</a></li>
                        <li><a class="dropdown-item" role="button" v-on:click="logout">Logout</a></li>
                    </ul>
                </li>

            </ul>
        </div>
    </div>
</template>

<script>
    import UploadModal from "./dashboard/chart/uploadModal.vue"

    export default {
        name: 'Header',
        data() {
            return {}
        },
        props: {
            id: null
        },
        methods: {
            logout: function () {

                fetch("/logout")
                    .then(response => {
                        if (response.status == 200) {
                            window.location.href = response.url;
                            console.log("Logout successsfully")
                        } else {
                            console.log("Logout failed")
                        }
                    })

            },
        },
        components: {
            UploadModal
        }
    }
</script>