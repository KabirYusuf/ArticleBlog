import Swal from 'sweetalert2';

export function handleErrors(error, fieldErrors = {}) {
  if (error.response && error.response.data && error.response.data.errors) {
    Object.entries(error.response.data.errors).forEach(([field, errorMessages]) => {
      if (fieldErrors[field]) {
        if (Array.isArray(errorMessages)) {
          fieldErrors[field].value = errorMessages.join(', ');
        } else {

          fieldErrors[field].value = errorMessages;
        }
      }
    });
    return;
  }


  let errorMessage = 'An error occurred';
  if (error.response && error.response.data) {
    errorMessage = error.response.data.Message || error.response.data;
  }

  Swal.fire({
    icon: 'error',
    title: 'Error',
    text: errorMessage
  });
}


