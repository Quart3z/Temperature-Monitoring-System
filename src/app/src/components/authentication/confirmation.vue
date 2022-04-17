<template>
    <div id="confirmation">
        <div class="p-3">
            <h3 class="text-center text-primary">Registration complete!</h3>
            <hr>
            <p class="text-center mb-5">The activation link have been sent to your email, click on it to activate your account</p>
            <p class="text-center mb-2">Going back to login page</p>
            <div class="text-center m-2">

                <div class="spinner-border text-primary spinner-border m-2" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
    
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'Confirmation',
        data() {
            return {}
        },
        methods: {

        },
        created() {

            setTimeout(() => {
                window.location.reload()
            }, 7000);

        }
    }
</script>