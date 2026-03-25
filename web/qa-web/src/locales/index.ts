import zhCN from './zh-CN';
import enUS from './en-US';

interface LanguagePack {
  home: {
    hero: {
      title: string;
      subtitle: string;
      features: {
        professional: string;
        realTime: string;
        privacy: string;
      };
      actions: {
        consult: string;
        doctors: string;
      };
    };
    statistics: {
      doctors: string;
      questions: string;
      active: string;
      sessions: string;
    };
    activeRooms: {
      title: string;
      subtitle: string;
      enter: string;
      online: string;
    };
  };
}

type Locale = 'zh-CN' | 'en-US';

const languagePacks: Record<Locale, LanguagePack> = {
  'zh-CN': zhCN,
  'en-US': enUS
};

class I18n {
  private currentLocale: Locale = 'zh-CN';
  private listeners: Array<() => void> = [];

  constructor() {
    const savedLocale = localStorage.getItem('appLocale') as Locale;
    if (savedLocale && Object.keys(languagePacks).includes(savedLocale)) {
      this.currentLocale = savedLocale;
    }
  }

  getLocale(): Locale {
    return this.currentLocale;
  }

  setLocale(locale: Locale): void {
    if (this.currentLocale !== locale) {
      this.currentLocale = locale;
      localStorage.setItem('appLocale', locale);
      this.notifyListeners();
    }
  }

  t(key: string): string {
    const keys = key.split('.');
    let value: any = languagePacks[this.currentLocale];
    
    for (const k of keys) {
      if (value && typeof value === 'object') {
        value = value[k];
      } else {
        return key;
      }
    }
    
    return typeof value === 'string' ? value : key;
  }

  onLocaleChange(callback: () => void): void {
    this.listeners.push(callback);
  }

  offLocaleChange(callback: () => void): void {
    this.listeners = this.listeners.filter(listener => listener !== callback);
  }

  private notifyListeners(): void {
    this.listeners.forEach(callback => callback());
  }
}

export const i18n = new I18n();
export type { Locale };
