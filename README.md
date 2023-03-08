# capacitor-mock-location-checker

Capacitor plugin to avoid location mocking

## Install

```bash
npm install capacitor-mock-location-checker
npx cap sync
```

## Example

```typescript
import { MockLocationChecker } from 'capacitor-mock-location-checker';


// Add exclution or white list
constructor(private platfrom: Platform) {
    this.platfrom.ready().then(() => {
      // Add exclution or white list
      this.checkMock(["com.transsion.fmradio", "com.reallytek.wg"]);

      
      // without white list
      this.checkMock([]);
    }
    )
  }

checkMock = async (whiteList: Array<string>) => {
    const checkResult = await MockLocationChecker.checkMock({whiteList});

    console.log('Mock Location Check Result:');
    console.log('Is Mock Location: ', checkResult.isMock);
    console.log('Messages: ', checkResult.messages);
    console.log('Indicated Apps: ', checkResult.indicated.join("\n"));


    // Open Detail Firts Indicated App
    if (checkResult.indicated != null) {
      await MockLocationChecker.goToMockLocationAppDetail({ packageName: checkResult.indicated[0] });
    }
  };
```

## API

<docgen-index>

* [`checkMock(...)`](#checkmock)
* [`goToMockLocationAppDetail(...)`](#gotomocklocationappdetail)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkMock(...)

```typescript
checkMock(options: { whiteList: Array<string>; }) => Promise<CheckMockResult>
```

| Param         | Type                                  | Description                                 |
| ------------- | ------------------------------------- | ------------------------------------------- |
| **`options`** | <code>{ whiteList: string[]; }</code> | : <a href="#array">Array</a>&lt;string&gt;} |

**Returns:** <code>Promise&lt;<a href="#checkmockresult">CheckMockResult</a>&gt;</code>

--------------------


### goToMockLocationAppDetail(...)

```typescript
goToMockLocationAppDetail(options: { packageName: string; }) => Promise<void>
```

| Param         | Type                                  | Description |
| ------------- | ------------------------------------- | ----------- |
| **`options`** | <code>{ packageName: string; }</code> | : string}   |

--------------------


### Interfaces


#### CheckMockResult

| Prop            | Type                                                  |
| --------------- | ----------------------------------------------------- |
| **`isMock`**    | <code>boolean</code>                                  |
| **`messages`**  | <code>string</code>                                   |
| **`indicated`** | <code><a href="#array">Array</a>&lt;string&gt;</code> |

</docgen-api>
