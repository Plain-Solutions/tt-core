Changelog
=========
###TT Core 2.0.0

* Introduced `LexxDataFetcher` - an instance of ADM working with SSU internal database. 
* `SSUDataFetcher` is deprecated and removed from distribution. We plan to bring it back as soon as we clean everything up. Or not. It cannot provide complete data in discrete format as it does LDF.
* Refactored `SSUSQLManager` to work with objects of same structue - lsit of some compound object. Only 6 methods from version 1.0 are preserved!  
	+ Totally reworked timetable fetching from the database. The assembly of `TTEntity`, `TTDayEntity` and `TTLesson` create transparent and clear for understanding structure of object to be converted as they are.
	+ Updated safety checks.
* All the entities and entity-like classes are now stored in `entity` package
* Updated database scheme with Lexx-like structure.
* Implemented non-empty groups fetching. Later, the respective API call will be added. It allows to fetch list of groups which have timetable filled.
* Global updates and improvements in classes in the fashion of reducing unused and extra elements:
	+ `AbstractDataManager`:
		+ Removed: `getFormattedString`, `deliverGlobalURL`, `getGroupID`, `getJSONConverter`, `putTT(departmentTaag, groupID)`
		+ `getTT(int id)` moved to `SSUDM` and became private
	+ `AbstractDataConverter`:
		+ `convertGroupName` - as removed `getGroupID`
		+ raw backwards converters are also removed
	+ `AbstractQueries`:
		+ `qGetDepartmentTagByID` ,`...NameByID`, `TagByName` are also removed.
	+ `AbstractSQLManager`:
		+ `getLastID` and all the checks moved to `SSUSQLManager` and became private.		
		


=======

###TT Core 1.2.6

* Added general-purposed K-V converter (Map to some (JSON) string) to avoid extra dependencies for TT Platform


###TT Core 1.2.5

* Added adequate testing module with correct independent tests with JUnit 4.
* TTStatus rewritten to return actual messages.
* Packages naming refactoring: `ssutt` replaced with `tt`
* `org.tt.core.dm.AbstractDataManager` now has new methods:
	 + `getTT(String departmentTag, String groupName)` to resemble TT API query and avoid conversion from name and tag to inner DB groupID on high level.
	 + `deliverDBProvider(AbstractSQLManager sqlm)` for pre-configured SQLManagers (with initializedQueries provider).
	 + `putTT` - with the same reason as `getTT`
	 + `deliverGlobalURL(String globalURLString)` - now parsed URL is placed in `DataManager`
* Reworked logics of `AbstractDataFetcher`. Now it uses method `fetch` to unify fetching any data from any URL or even file (for unit testing purposes).
* Due to `fetch` updated methods params in `AbstractDataFetcher`. Backwards compatibility with `TT Platform 1.0.3` is lost.

###TT Core 1.2.0

* Added `org.ssutt.dm.json` (now is `org.ssutt.dm.convert.json`) - transfered from TT Platform - package to transform data from TTDataManager into JSON Strings.
* `JSONConverter` now is an instance of `AbstractDataConverter` - interface to provide various output to user-agent in TT Platform. With this abstraction we can get not only JSON-formatted output, but anything, that can be converted to `String` 	
* Introduced `TTData` - an unified class to deliver data from DataManager to user-agent. It has only two fields  - `httpCode` and `message`, where we can store all the needed information about operation. Thus, we don't need to analyze the contents of message to get this or that error code in TT Platform servlets.
* As new platform-wide classes like `TTData` were introduced, all the interfaces are now named with **Abstract**. In a breif way:
	+ All the classes that don't have to be overloaded are now start with **TT**.
	+ All the interfaces, which are overloaded for SSU now start with **Abstract**.
* `org.ssutt.core.fetch.entities` renamed to `html` - as we now have `org.ssutt.dm.convert.json.entites` we don't want to mess up.
* Made all `AbstractDataManager` methods return `TTData` instance. 
* Encapsulated all the throws in `AbstractDataManager`.
* Created `TTStatus` enum to keep labels of states, errors and modules. 
* Now requires Google `gson` to parse JSON. 


###TT Core 1.1.0 (squashed into 1.2.0)

* Switched versioning system from `X.X.BUILD` to `X.X.PATCH` as it becomes to hard to watch after Travis CI build system and our own CI/CD Server.
* Updated JavaDoc to provide more detailed and specific information.
* Created this file.
* `TTDataFetcher`:
	+ Removed accessors as encapsulated formatting URL from `TTDataManager` to `DataFetcher`. 
	+ Moved `exclusions` and `globalScheduleURL` to `SSUTTDataFetcher` as it is specification of SSU website and should not be defined outside of the DF.
* `TTDataManager`:
	+ Now `deliverDataFetcherProvider()` requiries `TTDataFetcher` implementation as a parameter. This way we can specify DF for any university outside the TT Core library and create real polymorphism. 
	+ The same with `deliverDBProvider()`. Now requires `TTSQLManager` implementation and `Queries` implmenetation. This also allows to use TT Core for not only SSU TT Platform and not only with H2DB.
	+ `getTT` now returns `List<String[]>` to provide working with raw strings data across DM fully and delegate JSON-formatting to TT Platform classes as new return format of timetables is used. See api_refernce in TT Platform repository (link later) fore more information on Public API.
	+ `getDepartments` now returns `Map<String, Map<String, String>` to provide extensions to existing information about departments and make it more configurabl and simplier to convert to JSON.
* `TTSQLManager`:
    + Added `setQueries` to provide `Queries` instance with their definition.
    + `getDepartments` return now `Map<String, Map<String, String>>` instead of `Map<String, String>` to provide extensions to data about departments. key is tag of department, value is all the data for this or that department (phones, for instance). Entry tagged by `name` is displayable name in Russian. The list is sorted by **names**.
* `org.ssutt.core.dm.entities` has `TableEntity` and this factory <b>no more</b> due to updates in `TTDataManager.getTT`
* `org.ssutt.core.dm.entities` and `org.ssutt.core.dm.TableParser` moved to `org.ssutt.core.fetch.entities` package as they handle only HTML data manipulations.


###TT Core 1.0.35

* First release
* Please refer [our release page](https://github.com/Plain-Solutions/tt-core/releases/tag/1.0.35)