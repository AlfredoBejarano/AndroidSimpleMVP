SimpleMVP
===================


MVP Architechture made simple for android apps.

----------


Simple?
-------------

Usually, MVP implementations requires a lot of files and in the long run it makes app maintainability a mess.

**SimpleMVP** defines a base class for Presenters and a base interface for Views, which makes the integration fast.

And what about the Repository layer?
-------------

In this library, the repository layer is handled by an Interface using the [api definitions of **Retrofit 2**](http://square.github.io/retrofit/#api-definitions) and parses the Data Transfer Objects *DTO* using **Google's GSON**.

*SimpleMVP handles the initalization of Retrofit and the especification of the GSON Converter for you*

Reactive Programming (Rx) ?
-------------
Instead of adding more dependencies to your project like **RxJava** or **RxAndroid**, SimpleMVP uses the Callback<> Interface from Retrofit to report to the **SimpleView** implementation class the state of the request, making the library lighter but still idealizing the fundamentals of *Reactive Programming*.

Download
-------------
[Download AAR file](https://github.com/AlfredoBejarano/AndroidSimpleMVP/releases/download/3.0/simplemvp-release.aar)

Licence
-------------
> Licensed under the Apache License, Version 2.0 (the "License"); you
> may not use this file except in compliance with the License. You may
> obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
> implied. See the License for the specific language governing
> permissions and limitations under the License.
