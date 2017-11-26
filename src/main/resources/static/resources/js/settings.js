
function updatePasswordStatus() {
  const newPassword = $('.change-password .new-password');
  const confirmPassword = $('.change-password .confirm-password');

  if (validatePassword(newPassword, confirmPassword)) {
    $('.change-password .passwords-differ').hide();
  } else {
    $('.change-password .passwords-differ').show();
  }

  if (weakPassword(newPassword)) {
    $('.change-password .weak-password').show();
  } else {
    $('.change-password .weak-password').hide();
  }
}

$(function() {
  const newPassword = $('.change-password .new-password');
  const confirmPassword = $('.change-password .confirm-password');
  newPassword.on('change', updatePasswordStatus);
  confirmPassword.on('change', updatePasswordStatus);

  $('.change-password .weak-password').hide();
  $('.change-password .passwords-differ').hide();
});
