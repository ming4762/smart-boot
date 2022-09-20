
// export const themeColor = {
//   getAntdSerials (color: string) {
//     const lightens = new Array(9).fill(0).map(function (t, i) {
//       return client.varyColor.lighten(color, i / 10);
//     })
//     const colorPalettes = generate(color)
//     const rgb = client.varyColor.toNum3(color.replace('#', '')).join(',')
//     return lightens.concat(colorPalettes).concat(rgb)
//   },
//   changeColor (newColor: string) {
//     const options = {
//       newColor: this.getAntdSerials(newColor),
//       changeUrl (cssUrl: string) {
//         return`/${cssUrl}`
//       }
//     }
//     console.log(options)
//     return client.changer.changeColor(options, Promise)
//   }
// }


export const updateColorWeak = (colorWeak: any) => {
  const app = document.body.querySelector('#app')!
  colorWeak ? app.classList.add('colorWeak') : app.classList.remove('colorWeak')
}
