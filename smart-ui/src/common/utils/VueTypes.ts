import type { PropType } from 'vue'
// @ts-ignore
import isPlainObject from 'lodash/isPlainObject'
import { toType, isInteger, warn, isArray, isFunction, getType, validateType } from './utils'

const VuePropTypes = {
  get any() {
    return toType('any', {
      type: null
    })
  },

  get func() {
    return toType('function', {
      type: Function as PropType<Function>
    }).def!(currentDefaults.func)
  },

  get bool() {
    return toType('boolean', {
      type: Boolean as PropType<boolean>
    }).def(currentDefaults.bool)
  },

  get string() {
    return toType('string', {
      type: String as PropType<string>
    }).def(currentDefaults.string)
  },

  get number() {
    return toType('number', {
      type: Number as PropType<number>
    }).def(currentDefaults.number)
  },

  get array() {
    return toType('array', {
      type: Array as PropType<Array<any>>
    }).def(currentDefaults.array)
  },

  getObject() {
    return toType('object', {
      type: Object as PropType<object>
    }).def(currentDefaults.object)
  },

  get integer() {
    return toType('integer', {
      type: Number as PropType<number>,
      validator: (value: any) => {
        return isInteger(value)
      }
    })
  },

  get symbol() {
    return toType('symbol', {
      type: null,
      validator: (value: any) => {
        return typeof value === 'symbol'
      }
    })
  },

  custom(validatorFn: Function) {
    // eslint-disable-next-line prefer-rest-params
    const warnMsg = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'custom validation failed'

    if (typeof validatorFn !== 'function') {
      throw new TypeError('[VueTypes error]: You must provide a function as argument')
    }

    return toType(validatorFn.name || '<<anonymous function>>', {
      validator: function validator() {
        // eslint-disable-next-line prefer-rest-params,prefer-spread
        const valid = validatorFn.apply(undefined, arguments)
        if (!valid) warn(this._vueTypes_name + ' - ' + warnMsg)
        return valid
      }
    })
  },

  oneOf(arr: Array<any>) {
    if (!isArray(arr)) {
      throw new TypeError('[VueTypes error]: You must provide an array as argument')
    }
    const msg = 'oneOf - value should be one of "' + arr.join('", "') + '"'
    const allowedTypes = arr.reduce(function (ret, v) {
      if (v !== null && v !== undefined) {
        ret.indexOf(v.constructor) === -1 && ret.push(v.constructor)
      }
      return ret
    }, [])

    return toType('oneOf', {
      type: allowedTypes.length > 0 ? allowedTypes : null,
      validator: function validator(value: any) {
        const valid = arr.indexOf(value) !== -1
        if (!valid) warn(msg)
        return valid
      }
    })
  },

  instanceOf(instanceConstructor: any) {
    return toType('instanceOf', {
      type: instanceConstructor
    })
  },

  oneOfType(arr: Array<any>) {
    if (!isArray(arr)) {
      throw new TypeError('[VueTypes error]: You must provide an array as argument')
    }

    let hasCustomValidators = false

    const nativeChecks = arr.reduce(function (ret, type) {
      if (isPlainObject(type)) {
        if (type._vueTypes_name === 'oneOf') {
          return ret.concat(type.type || [])
        }
        if (type.type && !isFunction(type.validator)) {
          if (isArray(type.type)) return ret.concat(type.type)
          ret.push(type.type)
        } else if (isFunction(type.validator)) {
          hasCustomValidators = true
        }
        return ret
      }
      ret.push(type)
      return ret
    }, [])

    if (!hasCustomValidators) {
      // we got just native objects (ie: Array, Object)
      // delegate to Vue native prop check
      return toType('oneOfType', {
        type: nativeChecks
      }).def(undefined)
    }

    const typesStr = arr.map(function (type) {
      if (type && isArray(type.type)) {
        return type.type.map(getType);
      }
      return getType(type);
    }).reduce(function (ret, type) {
      return ret.concat(isArray(type) ? type : [type]);
    }, []).join('", "');

    return this.custom(function oneOfType(value: any) {
      const valid = arr.some(function (type) {
        if (type._vueTypes_name === 'oneOf') {
          // @ts-ignore
          return type.type ? validateType(type.type, value, true) : true;
        }
        // @ts-ignore
        return validateType(type, value, true);
      });
      if (!valid) warn('oneOfType - value type should be one of "' + typesStr + '"');
      return valid;
    }).def(undefined);
  },

  arrayOf (type: any) {
    return toType('arrayOf', {
      type: Array,
      validator: function validator(values: any) {
        const valid = values.every(function (value: any) {
          return validateType(type, value);
        });
        if (!valid) warn('arrayOf - value must be an array of "' + getType(type) + '"');
        return valid;
      }
    })
  },
  objectOf (type: any) {
    return toType('objectOf', {
      type: Object,
      validator: function validator(obj: any) {
        const valid = Object.keys(obj).every(function (key) {
          return validateType(type, obj[key])
        });
        if (!valid) warn('objectOf - value must be an object of "' + getType(type) + '"')
        return valid
      }
    })
  },
  shape (obj: any) {
    const keys = Object.keys(obj)
    const requiredKeys = keys.filter(function (key) {
      return obj[key] && obj[key].required === true
    })
    const type = toType('shape', {
      type: Object,
      validator: function validator(value: any) {
        // eslint-disable-next-line @typescript-eslint/no-this-alias
        const _this = this
        if (!isPlainObject(value)) {
          return false
        }
        const valueKeys = Object.keys(value)
        // check for required keys (if any)
        if (requiredKeys.length > 0 && requiredKeys.some(function (req) {
          return valueKeys.indexOf(req) === -1
        })) {
          warn('shape - at least one of required properties "' + requiredKeys.join('", "') + '" is not present')
          return false
        }
        return valueKeys.every(function (key) {
          if (keys.indexOf(key) === -1) {
            if (_this._vueTypes_isLoose === true) return true
            warn('shape - object is missing "' + key + '" property');
            return false
          }
          const type = obj[key];
          return validateType(type, value[key])
        })
      }
    })
    Object.defineProperty(type, '_vueTypes_isLoose', {
      enumerable: false,
      writable: true,
      value: false
    })
    Object.defineProperty(type, 'loose', {
      get: function get() {
        this._vueTypes_isLoose = true
        return this;
      },
      enumerable: false
    })
    return type
  }
}

const typeDefaults = function typeDefaults () {
  return {
    func: undefined,
    bool: undefined,
    string: undefined,
    number: undefined,
    array: undefined,
    object: undefined,
    integer: undefined
  }
}

let currentDefaults: any = typeDefaults()

Object.defineProperty(VuePropTypes, 'sensibleDefaults', {
  enumerable: false,
  set: function set(value) {
    if (value === false) {
      currentDefaults = {};
    } else if (value === true) {
      currentDefaults = typeDefaults();
    } else if (isPlainObject(value)) {
      currentDefaults = value;
    }
  },
  get: function get() {
    return currentDefaults;
  }
})

export default VuePropTypes
