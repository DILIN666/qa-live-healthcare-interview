import { createI18n } from 'vue-i18n'
import zhCN from './zh-CN'
import enUS from './en-US'

// 从localStorage获取语言设置，默认中文
const savedLanguage = localStorage.getItem('language') || 'zh-CN'

const i18n = createI18n({
  locale: savedLanguage,
  fallbackLocale: 'zh-CN',
  messages: {
    'zh-CN': zhCN,
    'en-US': enUS
  },
  legacy: false,
  globalInjection: true
})

export default i18n