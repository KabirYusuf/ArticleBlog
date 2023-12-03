import Swal from 'sweetalert2';


export function handleErrors(error) {
  
  Swal.fire({
    title: error.response.data,
    icon: 'error'
  });
}
