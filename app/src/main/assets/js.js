console.log('2')
document.querySelectorAll('button').forEach(element => element.replaceWith(element.cloneNode(true)));
console.log('3')