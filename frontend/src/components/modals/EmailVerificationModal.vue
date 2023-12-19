<script setup>
import {ref} from 'vue';
import {useModalStore} from '@/store/modalStore';
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'
import {verifyEmail} from '@/utility/Auth'
import Swal from 'sweetalert2'
import {useUserStore} from '@/store/userStore'
import {useRouter} from 'vue-router';
import {handleErrors} from '@/utility/handleErrors';

const userStore = useUserStore();
const router = useRouter();
const modalStore = useModalStore();

const verification = ref('')
const verificationError = ref('')

const message = ref('');

const handleChange = () => {
    verificationError.value = ''
    message.value = ''
}

const handleSubmit = async () => {

    if (!verification.value) {
        message.value = "Field can't be empty"
        return;
    }

    try {
        console.log(verification.value, 'verification.value')
        const response = await verifyEmail({
            verificationToken: verification.value,
        });

        userStore.logIn();

        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: response.data.message
        });

        modalStore.closeVerificationModal();

        router.push('/');

    } catch (error) {
        handleErrors(error, {
            verification: verificationError,
        });
    }
};

</script>
<template>
    <div class="modalContent__header">

        <p>Verification</p>

        <small class="modalContent__description">Check email for verification code</small>

    </div>
    <p v-if="message" style="color: red"> {{ message }} </p>

    <form @submit.prevent="handleSubmit">
        <InputField id="username" type="text" placeholder="Email Verification" v-model:value="verification"
                    class="input__field"
                    :focus="handleChange"/>
        <p v-if="verificationError" style="color: red;">{{ verificationError }}</p>

        <Button type="submit" class="form__submitButton">Verify</Button>
    </form>
</template>
