import { message } from 'ant-design-vue'
import { generate } from '@ant-design/colors'
// @ts-ignore
import client from 'webpack-theme-color-replacer/client'


export const themeColor = {
  getAntdSerials (color: string) {
    const lightens = new Array(9).fill(0).map(function (t, i) {
      return client.varyColor.lighten(color, i / 10);
    })
    const colorPalettes = generate(color)
    const rgb = client.varyColor.toNum3(color.replace('#', '')).join(',')
    return lightens.concat(colorPalettes).concat(rgb)
  },
  changeColor (newColor: string) {
    const options = {
      newColor: this.getAntdSerials(newColor),
      changeUrl (cssUrl: string) {
        return`/${cssUrl}`
      }
    }
    console.log('==================')
    return client.changer.changeColor(options, Promise)
  }
}

export const updateTheme = (newPrimaryColor: any) => {
  const hideMessage = message.loading('正在切换主题', 0)
  themeColor.changeColor(newPrimaryColor).then(() => hideMessage())
}

export const updateColorWeak = (colorWeak: any) => {
  const app = document.body.querySelector('#app')!
  colorWeak ? app.classList.add('colorWeak') : app.classList.remove('colorWeak')
}
